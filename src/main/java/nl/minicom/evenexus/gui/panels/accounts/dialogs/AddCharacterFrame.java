package nl.minicom.evenexus.gui.panels.accounts.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
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

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.exceptions.SecurityNotHighEnoughException;
import nl.minicom.evenexus.eveapi.exceptions.WarnableException;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.panels.accounts.AccountsPanel;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;
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

public class AddCharacterFrame extends CustomDialog {

	private static final long serialVersionUID = 5630084784234969950L;
	
	private static final Logger LOG = LoggerFactory.getLogger(AddCharacterFrame.class);
	
	private final AccountsPanel panel;
	private final Provider<ApiParser> apiParserProvider;
	private final CharacterPanel characterPanel;
	private final ImportManager importManager;
	private final Database database;
	private final BugReportDialog dialog;
	
	@Inject
	public AddCharacterFrame(AccountsPanel panel, 
			Provider<ApiParser> apiParserProvider,
			CharacterPanel characterPanel,
			ImportManager importManager,
			Database database,
			BugReportDialog dialog) {
		
		super(DialogTitle.CHARACTER_ADD_TITLE, 400, 375);
		this.apiParserProvider = apiParserProvider;
		this.characterPanel = characterPanel;
		this.importManager = importManager;
		this.database = database;
		this.panel = panel;
		this.dialog = dialog;
	}
	
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
layout.setHorizontalGroup(
	layout.createSequentialGroup()
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
	layout.setVerticalGroup(
		layout.createSequentialGroup()
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
				new Thread() {
					@Override
					public void run() {
						check.setEnabled(false);
						
						long keyId = Long.parseLong(keyIdField.getText());
						String vCode = verificationCodeField.getText();
						
						characterPanel.clear();
						List<EveCharacter> characters = checkCredentials(keyId, vCode);
						if (characters != null) {
							for (EveCharacter character : characters) {
								characterPanel.addCharacter(character);
							}
						}
						
						check.setEnabled(true);
					}
				}.start();
			}
		});
	
	add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						List<EveCharacter> characters = characterPanel.getSelectedCharacters();
						try {
							for (EveCharacter character : characters) {
								ApiKey apiKey = insertCharacter(character);
								importManager.addCharacterImporter(apiKey);
							}
						}
						catch (Exception e) {
							LOG.error(e.getLocalizedMessage(), e);
							dialog.setVisible(true);
						}
						finally {
							panel.reloadTab();
						}
					}
				}.start();
				dispose();
			}
		});
					
		setValidation(keyIdField, verificationCodeField, check);
	}

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
		if (characters == null || characters.isEmpty()) {
			LOG.warn("Account has no characters listed!");
			return null;
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
		return null;
	}

	protected List<EveCharacter> processParser(Node root, long keyId, String vCode) throws Exception {
		List<EveCharacter> characters = new ArrayList<EveCharacter>();
		if (ApiParser.isAvailable(root)) {
			Node node = root.get("result").get("key");
			if (node != null) {
				int mask = Integer.parseInt(node.getAttribute("accessMask"));
				
				boolean transactions = (mask & AccessMask.WALLET_TRANSACTIONS.getAccessMask()) != 0; 
				boolean journals = (mask & AccessMask.WALLET_JOURNAL.getAccessMask()) != 0; 
				boolean orders = (mask & AccessMask.MARKET_ORDERS.getAccessMask()) != 0; 
				boolean characterSheet = (mask & AccessMask.CHARACTER_SHEET.getAccessMask()) != 0; 
				boolean standings = (mask & AccessMask.STANDINGS.getAccessMask()) != 0; 
				
				if (transactions && journals && orders && characterSheet && standings) {
					Node subNode = node.get("rowset");
					for (int i = 0; i < subNode.size(); i++) {
						EveCharacter character = processRow(subNode, i, keyId, vCode);
						if (character != null) {
							characters.add(character);
						}
					}
				}
				else {
					throw new SecurityNotHighEnoughException();
				}
			}
		}
		return characters;
	}		

	private EveCharacter processRow(Node root, int index, long keyId, String vCode) throws SQLException {
		EveCharacter character = null;
		if (root.get(index) instanceof Node) {
			Node row = (Node) root.get(index);
			if (row != null && row.getTag().equals("row")) {
				long characterId = Long.parseLong(row.getAttribute("characterID"));
				String characterName = row.getAttribute("characterName");
				long corporationId = Long.parseLong(row.getAttribute("corporationID"));
				String corporationName = row.getAttribute("corporationName");
				character = new EveCharacter(keyId, vCode, characterId, characterName, corporationId, corporationName);
			}
		}
		return character;
	}

	private void setValidation(final JTextField userIDField, final JTextField apiKeyField, final JButton check) {
		
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
					&& ValidationRule.isLetterOrDigit(apiKeyField.getText());
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
	
	
	public enum AccessMask{
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
		
		private AccessMask(int accessMask) {
			this.accessMask = accessMask;
		}
		
		public int getAccessMask() {
			return accessMask;
		}
	}
}
