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


# Development Setup
* start gmapszTest with maven jetty plugin with the current SNAPSHOT version
* after modifying js, run gradle task `install` to install jar into local maven repository
wait for several seconds, jetty will restart to reload jar

# Build Job
[jenkins2 - Build_ZK_Addons](http://jenkins2/job/Build_ZK_Addons/)
