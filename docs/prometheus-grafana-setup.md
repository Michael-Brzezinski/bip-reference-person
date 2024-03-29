#  Prometheus Metrics access via Grafana

## Capability (Metrics Monitoring : Prometheus and Grafana)
Prometheus and Grafana can be used to monitor a vast range of applications. For collecting metrics, we shall be targeting the Spring Boot and Spring Cloud enabled REST application. The application exposes a metrics endpoint for Prometheus at "http://localhost:8080/actuator/prometheus" via Spring Boot Actuator.

#### Local Endpoints

See [Quick Start Guide](quick-start-guide.md) to run docker containers for local-int profile. You can then access Prometheus server and Grafana.

- Prometheus metrics can be accessed at: http://localhost:8080/actuator/prometheus

- Prometheus server can be accessed at: http://localhost:9090/graph

- Grafana can be accessed at: http://localhost:3000/?orgId=1. Default username and password are "admin/admin" 


## Add/View Prometheus as a Data Source in Grafana

- Log into Grafana with the username and password configured (the default is admin/admin)

- Click the gear icon in the left sidebar and from the menu select "Data Sources." 

- Click on "Add Data Source." This will open a page to add a data source. Prometheus should have been defined as a datasource already:
<img alt="Grafana Datasources" src="/docs/images/Grafana-Datasources.jpg" width="80%" height="80%"/>

- Give a suitable name to this new data source, as this will be used while creating visualizations:
<img alt="Grafana Datasource Details" src="/docs/images/Grafana-Datasource-Details.jpg" width="80%" height="80%"/>
	- Select Prometheus in the "type" drop down.
	- Local URL `http://localhost:9090` as Prometheus runs locally on port 9090.
	- Fill other details if you have any security or HTTP related settings.
	- Click "Save & Test."
	- If Grafana is able to make connections to Prometheus instance with the details provided, then you will get a message saying "Data source is working." If you get any errors, review your values.

## Dashboard

- We can access the home page for the dashboard:
	<img alt="Grafana Dashboard" src="/docs/images/Grafana-Dashboard.jpg" width="80%" height="80%"/>
	
	JVM Micrometer and Hystrix dashboards are available by default

- A new dashboard can be created based on the requirement using below steps:
	- Click on "Create your first dashboard" tab. Then click on one of the options like Graph or Singlestat or Table etc  
	<img alt="Grafana New Dashboard" src="/docs/images/Grafana-NewDashBoard.jpg" width="80%" height="80%"/>
	
	- Select a particular option as above to look at the required stats.
