var restUri = {
//	menu : {
//		app : getRestBaseUri() + '/menu/app',
//		user : getRestBaseUri() + '/menu/user'
//	},
	server:{
		list: getRestBaseUri() + '/server/list',
		table: getRestBaseUri() + '/server/table',
		item: getRestBaseUri() + '/server/{serverId}',
		save: getRestBaseUri() + '/server/save',
		del: getRestBaseUri() + '/server/delete/{serverId}'
	},
	user:{
		list: getRestBaseUri() + '/user/list',
		table: getRestBaseUri() + '/user/table',
		save: getRestBaseUri() + '/user/save',
		del: getRestBaseUri() + '/user/delete/{userId}'
	},
	table:{
		list: getRestBaseUri() + '/table/list',
		list_for_server_active: getRestBaseUri() + '/table/server/{serverId}/active',
		list_for_server: getRestBaseUri() + '/table/server/{serverId}/table/list',
		table: getRestBaseUri() + '/table/table',
		save: getRestBaseUri() + '/table/save'
	},
	connection:{
		list: getRestBaseUri() + '/connection/list',
		table: getRestBaseUri() + '/connection/table',
		save: getRestBaseUri() + '/connection/save'
	},
	menu: {
		user: getRestBaseUri() + '/menu/user',
		app: getRestBaseUri() + '/menu/app'
	}
	
};
