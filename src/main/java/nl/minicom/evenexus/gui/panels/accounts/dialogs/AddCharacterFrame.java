package nl.minicom.evenexus.gui.panels.accounts.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.exceptions.SecurityNotHighEnoughException;
import nl.minicom.evenexus.eveapi.exceptions.WarnableException;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.panels.accounts.AccountsPanel;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.titles.AddCharacterTitle;
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * This {@link AddCharacterFrame} class is a special {@link CustomDialog}
 * which allows the user to add one or more {@link EveCharacter}s to the 
 * database.
 * 
 * @author michael
 */
public class AddCharacterFrame extends CustomDialog {

	private static final long serialVersionUID = 5630084784234969950L;

	private static final Logger LOG = LoggerFactory.getLogger(AddCharacterFrame.class);

	private final AccountsPanel panel;
	private final Provider<ApiParser> apiParserProvider;
	private final CharacterPanel characterPanel;
	private final ImportManager importManager;
	private final Database database;
	private final BugReportDialog dialog;

	/**
	 * This constructs a new {@link AddCharacterFrame}.
	 * 
	 * @param panel
	 * 		The {@link AccountsPanel}.
	 * 
	 * @param apiParserProvider
	 * 		A provider of {@link ApiParser} object.
	 * 
	 * @param characterPanel
	 * 		The {@link CharacterPanel}.
	 * 
	 * @param importManager
	 * 		The {@link ImportManager}.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param dialog
	 * 		The {@link BugReportDialog}.
	 */
	@Inject
	public AddCharacterFrame(AccountsPanel panel,
			Provider<ApiParser> apiParserProvider,
			CharacterPanel characterPanel, ImportManager importManager,
			Database database, BugReportDialog dialog) {

		super(new AddCharacterTitle(), 400, 375);
		this.apiParserProvider = apiParserProvider;
		this.characterPanel = characterPanel;
		this.importManager = importManager;
		this.database = database;
		this.panel = panel;
		this.dialog = dialog;
	}

	/**
	 * This method initializes this {@link CustomDialog}.
	 */
	public void initialize() {
		buildGui();
		setVisible(true);
	}

	@Override
	public void createGui(JPanel guiPanel) {
		final JLabel userIDLabel = new JLabel("Key ID");
		final JLabel apiKeyLabel = new JLabel("Verification Code");
		final JTextField keyIdField = new JTextField();
		final JTextField verificationCodeField = new JTextField();
		final JButton check = new JButton("Check API credentials");
		final JButton add = new JButton("Add character(s)");

		characterPanel.initialize(add);

		check.setEnabled(false);
		add.setEnabled(false);

		userIDLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		keyIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		apiKeyLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		verificationCodeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		characterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		check.setMaximumSize(new Dimension(160, 32));
		add.setMaximumSize(new Dimension(160, 32));

		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(userIDLabel)
						.addComponent(keyIdField)
						.addComponent(apiKeyLabel)
						.addComponent(verificationCodeField)
						.addComponent(check)
						.addComponent(characterPanel)
						.addComponent(add)
				)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(userIDLabel)
				.addComponent(keyIdField)
				.addGap(7)
				.addComponent(apiKeyLabel)
				.addComponent(verificationCodeField)
				.addGap(7)
				.addComponent(check)
				.addGap(7)
				.addComponent(characterPanel)
				.addGap(7)
				.addComponent(add)
		);

		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new CheckApi(check, keyIdField, verificationCodeField));
			}
		});

		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new AddApi());
			}
		});

		setValidation(keyIdField, verificationCodeField, check);
	}

	/**
	 * This method inserts a new {@link ApiKey} entity to the database
	 * based on the provided {@link EveCharacter}.
	 * 
	 * @param character
	 * 		The {@link EveCharacter} to add to the database.
	 * 
	 * @return
	 * 		The generated and persisted {@link ApiKey}.
	 */
	@Transactional
	protected ApiKey insertCharacter(final EveCharacter character) {
		Session session = database.getCurrentSession();
		ApiKey api = new ApiKey();
		api.setVerificationCode(character.getVerificationCode());
		api.setCharacterId(character.getCharacterId());
		api.setCharacterName(character.getCharacterName());
		api.setCorporationId(character.getCorporationId());
		api.setCorporationName(character.getCorporationName());
		api.setKeyId(character.getKeyId());
		session.saveOrUpdate(api);
		return api;
	}

	private List<EveCharacter> checkCredentials(long keyId, String vCode) {
		List<EveCharacter> characters = getCharacterList(keyId, vCode);
		if (characters.isEmpty()) {
			LOG.warn("Account has no characters listed!");
		}

		return characters;
	}

	private List<EveCharacter> getCharacterList(long keyId, String vCode) {
		try {
			Map<String, String> request = new HashMap<String, String>();
			request.put("keyID", Long.toString(keyId));
			request.put("vCode", vCode);

			ApiParser parser = apiParserProvider.get();
			Node root = parser.parseApi(Api.KEY_INFO, null, request);
			return processParser(root, keyId, vCode);
		} 
		catch (WarnableException e) {
			LOG.warn(e.getLocalizedMessage(), e);
		} 
		catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
		return Lists.newArrayList();
	}

	private List<EveCharacter> processParser(Node root, long keyId, String vCode) throws Exception {
		if (ApiParser.isAvailable(root)) {
			Node node = root.get("result").get("key");
			if (node != null) {
				return processKey(keyId, vCode, node);
			}
		}
		return Lists.newArrayList();
	}

	private List<EveCharacter> processKey(long keyId, String vCode, Node node) 
			throws SQLException, SecurityNotHighEnoughException {

		int mask = Integer.parseInt(node.getAttribute("accessMask"));

		boolean transactions = (mask & Permission.WALLET_TRANSACTIONS
				.getAccessMask()) != 0;
		boolean journals = (mask & Permission.WALLET_JOURNAL.getAccessMask()) != 0;
		boolean orders = (mask & Permission.MARKET_ORDERS.getAccessMask()) != 0;
		boolean characterSheet = (mask & Permission.CHARACTER_SHEET
				.getAccessMask()) != 0;
		boolean standings = (mask & Permission.STANDINGS.getAccessMask()) != 0;

		List<EveCharacter> characters = Lists.newArrayList();
		if (transactions && journals && orders && characterSheet && standings) {
			Node subNode = node.get("rowset");
			for (int i = 0; i < subNode.size(); i++) {
				EveCharacter character = processRow(subNode, i, keyId, vCode);
				if (character != null) {
					characters.add(character);
				}
			}
		} else {
			throw new SecurityNotHighEnoughException();
		}
		
		return characters;
	}

	private EveCharacter processRow(Node root, int index, long keyId,
			String vCode) throws SQLException {
		EveCharacter character = null;
		if (root.get(index) instanceof Node) {
			Node row = (Node) root.get(index);
			if (row != null && row.getTag().equals("row")) {
				long characterId = Long.parseLong(row
						.getAttribute("characterID"));
				String characterName = row.getAttribute("characterName");
				long corporationId = Long.parseLong(row
						.getAttribute("corporationID"));
				String corporationName = row.getAttribute("corporationName");
				character = new EveCharacter(keyId, vCode, characterId,
						characterName, corporationId, corporationName);
			}
		}
		return character;
	}

	private void setValidation(final JTextField userIDField,
			final JTextField apiKeyField, final JButton check) {

		ValidationRule userIdRule = new ValidationRule() {
			@Override
			public boolean isValid() {
				return ValidationRule.isNotEmpty(userIDField.getText())
						&& ValidationRule.isDigit(userIDField.getText());
			}
		};

		ValidationRule apiKeyRule = new ValidationRule() {
			@Override
			public boolean isValid() {
				return ValidationRule.hasLength(apiKeyField.getText(), 64)
						&& ValidationRule
								.isLetterOrDigit(apiKeyField.getText());
			}
		};

		new StateRule(userIdRule, apiKeyRule) {
			@Override
			public void onValid(boolean isValid) {
				check.setEnabled(isValid);
			}
		};

		userIDField.addKeyListener(new ValidationListener(userIdRule));
		apiKeyField.addKeyListener(new ValidationListener(apiKeyRule));
	}

	/**
	 * The {@link Permission} enum defines which permissions have which masks.
	 *
	 * @author michael
	 */
	public enum Permission {
		ACCOUNT_BALANCE(1), 
		ASSET_LIST(2), 
		CALENDAR_EVENT_ATTENDEES(4), 
		CHARACTER_SHEET(8), 
		CONTACT_LIST(16), 
		CONTACT_NOTIFICATIONS(32), 
		FAC_WAR_STATS(64), 
		INDUSTRY_JOBS(128), 
		KILL_LOG(256), 
		MAIL_BODIES(512), 
		MAILING_LISTS(1024), 
		MAIL_MESSAGES(2048), 
		MARKET_ORDERS(4096), 
		MEDALS(8192), 
		NOTIFICATIONS(16384), 
		NOTIFICATION_TEXTS(32768), 
		RESEARCH(65536), 
		SKILL_IN_TRAINING(131072), 
		SKILL_QUEUE(262144), 
		STANDINGS(524288), 
		UPCOMING_CALENDAR_EVENTS(1048576), 
		WALLET_JOURNAL(2097152), 
		WALLET_TRANSACTIONS(4194304), 
		CHARACTER_INFO_PRIVATE(8388608), 
		CHARACTER_INFO_PUBLIC(16777216), 
		ACCOUNT_STATUS(33554432), 
		CONTRACTS(67108864);

		private final int accessMask;

		private Permission(int accessMask) {
			this.accessMask = accessMask;
		}

		/**
		 * @return
		 * 		The mask of the {@link Permission}.
		 */
		public int getAccessMask() {
			return accessMask;
		}
	}

	/**
	 * This {@link Runnable} checks the authentication, security of the 
	 * specified account.
	 *
	 * @author michael
	 */
	private class CheckApi implements Runnable {
		private final JButton button;
		private final JTextField keyIdField;
		private final JTextField vCodeField;

		public CheckApi(JButton button, JTextField keyIdField,
				JTextField vCodeField) {
			this.button = button;
			this.keyIdField = keyIdField;
			this.vCodeField = vCodeField;
		}

		@Override
		public void run() {
			button.setEnabled(false);

			long keyId = Long.parseLong(keyIdField.getText());
			String vCode = vCodeField.getText();

			characterPanel.clear();
			List<EveCharacter> characters = checkCredentials(keyId, vCode);
			for (EveCharacter character : characters) {
				characterPanel.addCharacter(character);
			}

			button.setEnabled(true);
		}
	}
	
	/**
	 * This {@link Runnable}, adds the specified API to the database.
	 *
	 * @author michael
	 */
	private class AddApi implements Runnable {
		@Override
		public void run() {
			try {
				List<EveCharacter> characters = characterPanel.getSelectedCharacters();
				for (EveCharacter character : characters) {
					ApiKey apiKey = insertCharacter(character);
					importManager.addCharacterImporter(apiKey);
				}
			} catch (Exception e) {
				LOG.error(e.getLocalizedMessage(), e);
				dialog.setVisible(true);
			} finally {
				panel.reloadTab();
				dispose();
			}
		}
	}

}
