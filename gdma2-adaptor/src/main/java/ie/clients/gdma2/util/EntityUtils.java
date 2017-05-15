package ie.clients.gdma2.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.User;

public class EntityUtils {

	private static final Logger logger = LoggerFactory.getLogger(EntityUtils.class);
	public static final String PASSWORD_MASK = "********";

	public static void applyColumnRules(Column column) {
		logger.info("applyRules for column:" + column.getName());

		if (column.isPrimarykey()) {
			column.setDisplayed(true);
			column.setAllowUpdate(false); //but if not autoincrement then me have to allow update
			column.setNullable(false);
			column.setSpecial("N");
		}

		if (column.isAllowInsert()) {
			column.setDisplayed(true);
		}

		if (column.isAllowUpdate()) {
			column.setDisplayed(true);
		}


		if (org.springframework.util.StringUtils.hasText(column.getSpecial())) {
			if ("U".equals(column.getSpecial())) {
				// check column type
				if (!SQLUtil.isText(column.getColumnTypeString())) {
					throw new IllegalArgumentException("Column [" + column.getName() + "] of type ["
							+ column.getColumnTypeString()
							+ "] can not be set to 'special type User' as is is not of type text.");
				}
				logger.info("special column USER set for: " + column.getName() + ", Insert or Update are now not allowed manually,"
						+ " but will be done automatically during add/update record in data module");
				column.setAllowInsert(false);
				column.setAllowUpdate(false);

			}
			if ("D".equals(column.getSpecial())) {
				// check column type
				if (!SQLUtil.isDate(column.getColumnTypeString())) {
					throw new IllegalArgumentException("Column [" + column.getName()
							+ "] can not be set to 'special type Date' as is is not of type date.");
				}
				logger.info("special column DATE for: " + column.getName() + ", Insert or Update are now not allowed manually,"
						+ " but will be done automatically during add/update record in data module");
				column.setAllowInsert(false);
				column.setAllowUpdate(false);
			}
		}

	}

	/*server password should not be send via JSON response - mask them and display them in UI masked */
	public static void emptyPasswordForServers(List<Server> servers) {
		logger.info("emptyPasswordForServers");
		if( servers != null && !servers.isEmpty()){
			for(Server s: servers){
				emptyServerPassword(s);
			}
		}
	}


	public static void emptyServerPassword(Server server){
		if(server != null) {
			if(StringUtils.isNotBlank(server.getPassword())){
				server.setPassword(PASSWORD_MASK);
				logger.info("emptying server password for serverId: " + server.getId());
			} else{
				logger.info("password is blank - no masking");
			}
		}

	}

	//Emptying the passwords for users
	public static void emptyPasswordsForUsers(List<User> users){
		logger.info("emptyPasswords for users");
		if( users != null && !users.isEmpty()){
			for(User u: users){
				emptyUserPassword(u);
			}
		}

	}

	public static void emptyUserPassword(User user){
		if( user != null) {
			if(StringUtils.isNotBlank(user.getPassword())){
				user.setPassword(PASSWORD_MASK);
				logger.info("emptying user password for user " + user.getUserName());
			} else {
				//logger.info("password is blank - no masking");
			}
		}
	}

}
