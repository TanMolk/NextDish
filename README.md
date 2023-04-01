# CSC8019 Group Coursework

---

## Abstract

It is aim to store code source for the whole project. This project's architecture is **Anteroposterior end separation**.

## Technology stacks

- **Backend**

| Name       | Version |
|------------|---------|
| Springboot | 2.7.9   |

- **Frontend**

| Name | Version |
|------|---------|
| Vue  | 3.2.47  |

## File Structure

- **Backend**

```text
src
|____test (store unit test)
|____main (store main code)
| |____resources
| | |____application.yaml (environment configuration file)
| |____java (main java code)
| | |____ncl.csc8019.group12 (all code in this package)
| | | | | |____BackendApplication.java (backend entrance)
```

- **Frontend**

```text
frontend
|____index.html (frontend entrance)
|____vite.config.js (vite configuration)
|____public
| |____favicon.ico (web page icon)
|____package.json (dependency config)
|____.env (environment configuration)
|____src (all source code of frontend)
```

## Code Standard

### Naming convention

1. All name should try to contain more information
   Bad example: method1, param1
   Good example: getCarWithColor, studentName.

2. File name should be PascalCase
   Bad example: usercontroller.java
   Good example: UserController.java

3. Name of method, variable should be camelCase
   Bad example: Params, ThisisAMeTHod
   Good example for method: queryUserDataWithId
   Good example for variable: studentName, locationOfUser
   Good example for constants variable: USER_API_KEY

4. Try to write much information to describe the author, the purpose, the aim and return of a class, a method or a
   variable.

### Code Quality

1. All function should be tested by unit test (normal case, exception case)
2. Try to solve problems IntelliJ tells and SonarLint tells (Please install sonarlint plugin firstly, could find in
   setting -> plugins)

### Others:

1. Api should follow restful style. [Restful](https://www.redhat.com/en/topics/api/what-is-a-rest-api)

## Run server

You should start backend server firstly and then start frontend

[Run Backend](./docs/Start_Backend.md)

[Run Frontend](./docs/Start_Frontend.md)