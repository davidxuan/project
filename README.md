# CSCI 578 Project

#### Project Description
The two recovery techniques we discussed in class, ACDC and ARC, are not suitable for recovering security architectural decisions which usually span more than one structural component. The purpose of this project is to implement changes to ACDC to address this issue.

We have chosen Apache Tomcat 8.0.47 for this project.

#### Security Decision

**[CVE-2017-12617](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2017-12617)**
  - Description: When running Apache Tomcat versions 9.0.0.M1 to 9.0.0, 8.5.0 to 8.5.22, 8.0.0.RC1 to 8.0.46 and 7.0.0 to 7.0.81 with HTTP PUTs enabled (e.g. via setting the readonly initialisation parameter of the Default servlet to false) it was possible to upload a JSP file to the server via a specially crafted request. This JSP could then be requested and any code it contained would be executed by the server.
  - [Revision](https://svn.apache.org/viewvc?view=revision&revision=1809921)


Even though *AbstractFileResourceSet* is dependent on *JrePlatform*, the two are in different clusters. As shown below:
![AbstractFileResource](resources/AbstractFileResourceSet&#32;Cluster.png)
![JREPlatform](resources/JRE&#32;Cluster.png)
When we took a closer look at the implementation of ACDC, we found the reason for this. The SubGraph module in ACDC searches for direct children of each node. When a component is not a direct child of any dominator, XXXXXXXXX


#### How We Solve the Problem

In order to resolve the limitation of SubGraph, we created a new node to include the components that were not clustered previously.

A list of files we modified: 

| File | Reason |
| --- | --- |
| [ACDC.java](src/acdc/ACDC.java)           | XXXX |
| [RSFOutput.java](src/acdc/RSFOutput.java) | XXXX |


We Also added a new file:

| File | Reason |
| --- | --- |
| [XXX.java](src/acdc/XXX.java) | XXXX |


Result cluster:
![Image]()

#### Visualization

![Cluster](resources/cluster_vis.png)