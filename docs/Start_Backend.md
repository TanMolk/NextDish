# Run Backend server

## With IntelliJ

1. Find the [application.yaml](../backend/src/main/resources/application.yaml),
   and replace "FILL_WITH_YOU_API_KEY_OF_GOOGLE_MAP" with you google api key
   ![1.png](static/1.png)
2. Fill the database information in the [application.yaml](../backend/src/main/resources/application.yaml), this
   database should run the sql in [tables.sql](../backend/sql/tables.sql) in advance.
3. Fill the email information in [application.yaml](../backend/src/main/resources/application.yaml), if you don't want
   to use email. Set "mail.test-connection" as false

4. Then find [BackendApplication.java](../backend/src/main/java/ncl/csc8019/group12/BackendApplication.java),
   run this backend application.
   ![1.png](static/2.png)

_This Google Map API key should have **Places API** permissions at least._