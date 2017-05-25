var restUri = {
	server:{
		list: getRestBaseUri() + '/server/list',
		table: getRestBaseUri() + '/server/table',
		item: getRestBaseUri() + '/server/{serverId}',
		save: getRestBaseUri() + '/server/save',
		del: getRestBaseUri() + '/server/delete/{serverId}',
		list_active: getRestBaseUri() + '/server/list/active'
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
		list_active: getRestBaseUri() + '/table/server/{id}/active',
		sync_table_server: getRestBaseUri() + '/table/{id}/metadata',
		table: getRestBaseUri() + '/table/server/{id}',
		save: getRestBaseUri() + '/table/save'
//		table_data:  getRestBaseUri() + '/column/data/read/table/{id}' /* DOESNT EXIST ANYMORE */
	},
	column:{
		sync: getRestBaseUri() + '/column/metadata/table/{id}',
		list: getRestBaseUri() + '/column/list',
		list_active: getRestBaseUri() + '/column/table/{id}/active',
		table: getRestBaseUri() + '/column/table/{id}',
		item: getRestBaseUri() + '/column/table/{id}/active',
		save: getRestBaseUri() + '/column/save'
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
	},
	datatable: {
		servers: getRestBaseUri() + '/datatable/servers',
		tables: getRestBaseUri() + '/datatable/tables/server/{id}',
		table: getRestBaseUri() + '/datatable/table/{id}',
		columnsMetaData: getRestBaseUri() + '/datatable/columns/table/{id}',
		dropdown: getRestBaseUri() + '/datatable/dropdown/display/{did}/store/{sid}',
		tableWithDropDown: getRestBaseUri() + '/datatable/tablewithdropdown/{id}',
		tablewithdropdowntest: getRestBaseUri() + '/datatable/tablewithdropdowntest/{id}',
		update: getRestBaseUri() + '/datatable/update/{serverId}/{tableId}',
		remove: getRestBaseUri() + '/datatable/delete/table/{id}'
	}
	
};
