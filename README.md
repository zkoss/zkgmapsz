# zkgmaps


The component used to represent [Google Map API](https://developers.google.com/maps/documentation/javascript/?csw=1).

Google Map API is a popular javascript library that lets you embed Google Maps in your own web pages.

zk gmapsz components wrap the Google Map API and very easy to use.

# License
 * [GPL 2.0](https://www.gnu.org/licenses/gpl-2.0.html)
 
# Documentation

Component References:

 - [Gmap](http://books.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps)
 - [Gimage](http://books.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps/Gimage)
 - [Ginfo](http://books.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps/Ginfo)
 - [Gmarker](http://books.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps/Gmarker)
 - [Gpolyline](http://books.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps/Gpolyline)
 - [Gpolygon](http://books.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps/Gpolygon)

# Issues
 Now we've transferred issues to [jira issue tracker](http://tracker.zkoss.org/browse/ZKGMAPS)

# Portable Development Setup
Run `mvn jetty:run` on gmapsz parent pom to build the library and launch the test app

# Automated / manual testing
After running test app (above), access testing panel at http://localhost:8080/gmapszTest/test2/
Use left-side navigation to select test case

## adding test cases
Create test case file under `/gmapszTest/src/main/webapp/test2`
Current test file name convention: ZKGMAPS-[tracker entry number].zul
Then add the test file to `/gmapszTest/src/main/webapp/test2/config.properties`
Run the project again and access `http://localhost:8080/gmapszTest/test2/` to run test

# Legacy Development Setup
* start gmapszTest with maven jetty plugin with the current SNAPSHOT version
* after modifying js, run gradle task `install` to install jar into local maven repository
wait for several seconds, jetty will restart to reload jar

# Release process
1. Update versions in all places, run [update_version.py](/gmapsz/update_version.py)
2. Update release note. 
get from [JIRA](https://tracker.zkoss.org/projects/ZKGMAPS?selectedItem=com.atlassian.jira.jira-projects-plugin:release-page&status=unreleased)
3. Build and publish to maven by 
[jenkins2 - Build_ZK_Addons](http://jenkins2/job/Build_ZK_Addons/)
