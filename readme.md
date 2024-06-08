### create mongo db
    
    show dbs
    use order-management

    db.createUser(
        {
            user: "admin",
            pwd: "admin",
            roles: [
                {
                    role: "userAdminAnyDatabase", 
                    db: "admin"
                },
                {
                    role: "readWriteAnyDatabase", 
                    db: "admin"
                }
            ]
        }
    )

## ARVORE DE DECISAO

https://lucid.app/lucidspark/0a8efe2d-6494-431e-a57a-a9a36303151a/edit?beaconFlowId=A7E29ED6EE3496E1&invitationId=inv_1744a5f8-b109-4fa4-8492-141137446a03&page=0_0#