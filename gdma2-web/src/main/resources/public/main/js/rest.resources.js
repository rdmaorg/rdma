var restUri = {
//	menu : {
//		app : getRestBaseUri() + '/menu/app',
//		user : getRestBaseUri() + '/menu/user'
//	},
	server:{
		data: getRestBaseUri() + '/server/data/tables',
		list: getRestBaseUri() + '/server/list',
		table: getRestBaseUri() + '/server/table',
		item: getRestBaseUri() + '/server/{serverId}',
		save: getRestBaseUri() + '/server/save',
		del: getRestBaseUri() + '/server/delete/{serverId}'
	},
	user:{
		list: getRestBaseUri() + '/user/list',
		table: getRestBaseUri() + '/user/table',
		item: getRestBaseUri() + '/user/id/{userId}',
		save: getRestBaseUri() + '/user/save',
		del: getRestBaseUri() + '/user/delete/{userId}'
	},
	table:{
		list: getRestBaseUri() + '/table/list',
		list_for_server_active: getRestBaseUri() + '/table/data/server/{id}',
		sync_table_server: getRestBaseUri() + '/table/{id}/metadata',
		table: getRestBaseUri() + '/table/server/{id}',
		save: getRestBaseUri() + '/table/save'
	},
	column:{
		sync: getRestBaseUri() + '/column/metadata/table/{id}',
		list: getRestBaseUri() + '/column/list',
		table: getRestBaseUri() + '/column/table/{id}',
		item: getRestBaseUri() + '/column/table/{id}/active'
	},
	userAcces:{
		table: getRestBaseUri() + '/access/table/{id}',
		update: getRestBaseUri() + '/access/update'
	},
	connection:{
		list: getRestBaseUri() + '/connection/list',
		table: getRestBaseUri() + '/connection/table',
		save: getRestBaseUri() + '/connection/save',
		del: getRestBaseUri() + '/connection/delete/{id}'
	},
	menu: {
		user: getRestBaseUri() + '/menu/user',
		app: getRestBaseUri() + '/menu/app'
	}
	
};
