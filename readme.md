**Dependencies**
1. jackson-annotations-2.9.0
2. jackson-core-2.9.6
3. jackson-databind-2.9.6
4. java-sdk 8
5. socrata API endpoint

**To build the application**

A runnable jar is provided to execute the application.  There is no need to build it. 

**To run the application**

A runnable jar was created from the project.  To run the application, go to the root of the directory and locate "ShowOpenFoodTrucks.jar".
From the command line, execute:

    java -jar ShowOpenFoodTrucks.jar

This will execute the application.  When the application executes, it will display the first ten food trucks that are open.  The user
will have three options:
1. Type 'q' to quit the application
2. Type 'n' to grab the next 10 food trucks
3. Type 'p' to grab the 10 previous food trucks


**Here are a list of things I would do differently for a full-scale web application**

For a full-scale web application, I would first understand the use case and how much scalling really needs to be done.
The more you scale up, the most complex and costly it becomes.  Ideally, I would keep it simple and design the system
to handle the expected workload for the next couple of year and readjust based on actual data usage.  

**General Idea of System**
    
    Client -> Load Balancer -> WebService

**Client**
- A Single Page Application written in React.  If there is a lot of ui state management, we can incorporate redux
- All the logic would exist on the client.  This includes
    - Talking to the endpoints
    - Instead of a console application, more thought would need to be put into the UI.  For example, how the information 
      will be rendered.  The look and feel of navigating around the application
    - I would have leverage off the Socratia SDK for the endpoints to avoid the overhead of extending/maintaing a custom client API to support all the endpoints. 
      Also there is less potential for errors with using the SDK.  
    - Add telemetry and logging to understand how the applicaiton is being used and determine if any issue have occured.  The information
      would also be useful in understanding how much scaling needs to be done.  We don't want to over scale if there is no need for it.
    - Since the endpoints have a quota limit, we would have to sign up for a token based on expected usage
    - Add caching to optimize the amount of request being sent out.  This will improve performance as well as save against the quota limit.

**Load Balancer**
- To help distribute the application workload to different servers.  If one service goes down, it would be easy to distribute the traffic to
  another service.  Also can help distribute load based on geo location and where the servers/datacenters are located.

**WebService**
- Application only talks to external services, so the job of the web service is really just to server the pages.  
- Depending on cost and performance, a CDN service can be used.  

**Toolset**
- Use npm to manage external opensource libraries
- Use webpack to bundle up all the javascript modules.  Can have different configuration for prod vs. dev
- Add unit test to validate client logic
- Use Typescript and Linting to make codebase more readable and manageable