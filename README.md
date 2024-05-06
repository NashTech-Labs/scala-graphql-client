# graphql-client-demo

This is a sample project that creates a GitHub client using [sbt-graphql](https://github.com/muuki88/sbt-graphql).

Steps to run
1. Clone the project
2. Run `sbt clean compile` - This will add auto-generated code for executing GrpahQL requests under the target directory
3. Add your GitHub Personal Access Token in the `GraphQLClient` object under the `Authorization` header
4. Run the Main app and you can see the results in the console. 


To create a new GraphQL request - create a valid GraphQL query and add it in the graphql folder as a separate query. This has already been done for 2 requests. We also need to make changes in the schema.graphql file. 
