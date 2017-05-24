package ie.clients.gdma2.domain.ui;

import java.io.Serializable;
import java.util.List;

/*


https://localhost/gdma2/rest/datatable/table/133?length=22&order[0][column]=618

{  
   "data":[  
      {  
         "rowNumber":1,
         "columns":[  
            {  
               "columnName":"country",
               "val":"France"
            },
            {  
               "columnName":"city",
               "val":"Nantes"
            },
            {  
               "columnName":"contactFirstName",
               "val":"Carine "
            },
            {  
               "columnName":"membership_id",
               "val":{  
                  "value":"101",
                  "did":2255,
                  "sid":2254,
                  "dropdownOptions":[  
                     [  
                        0,
                        103,
                        "gold"
                     ],
                     [  
                        1,
                        101,
                        "ordinary"
                     ],
                     [  
                        2,
                        102,
                        "silver"
                     ]
                  ]
               }
            },
            {  
               "columnName":"postalCode",
               "val":"44000"
            },
            {  
               "columnName":"salesRepEmployeeNumber",
               "val":1370
            },
            {  
               "columnName":"customerNumber",
               "val":103
            },
            {  
               "columnName":"gender_id",
               "val":{  
                  "value":"",
                  "did":2257,
                  "sid":2256,
                  "dropdownOptions":[  
                     [  
                        0,
                        23,
                        "Female"
                     ],
                     [  
                        1,
                        22,
                        "Male"
                     ],
                     [  
                        2,
                        25,
                        "Not Aplicable"
                     ],
                     [  
                        3,
                        24,
                        "Unkown"
                     ]
                  ]
               }
            },
            {  
               "columnName":"customerName",
               "val":"Atelier graphique"
            },
            {  
               "columnName":"phone",
               "val":"40.32.2555"
            },
            {  
               "columnName":"addressLine1",
               "val":"54, rue Royale"
            },
            {  
               "columnName":"creditLimit",
               "val":21000
            },
          
            {  
               "columnName":"contactLastName",
               "val":"Schmitt"
            },
            {  
               "columnName":"state",
               "val":null
            }
         ]
      },
      {  
         
		 "rowNumber":2,
		 . . .
		 
         ],
   "draw":0,
   "recordsTotal":122,
   "recordsFiltered":122
}

	*/


public class DropDownColumn implements Serializable {

	private String value;
	
	private int did;
	
	private int sid;
	
	private List dropdownOptions;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getDid() {
		return did;
	}
	
	public void setDid(int did) {
		this.did = did;
	}
	
	public int getSid() {
		return sid;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public List getDropdownOptions() {
		return dropdownOptions;
	}
	
	public void setDropdownOptions(List dropdownOptions) {
		this.dropdownOptions = dropdownOptions;
	}
	
}
