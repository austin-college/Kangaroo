package coursesearch.data.prefill

import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional
import coursesearch.CourseUtils

abstract class UpdateableDataService {

    protected static String dataRoot = "https://raw.github.com/austin-college/data/master"
    protected static int lastVersionUsed = 0;

    @Transactional
    def upgradeIfNeeded() {

        def dataFromServer = getDataFromServer();

        if (dataFromServer.version > lastVersionUsed) {

            // Run the upgrade.
            println "Upgrading ${name} to version ${dataFromServer.version}..."
            CourseUtils.runAndTime("${name} updated to version ${lastVersionUsed}") {
                upgradeAll(dataFromServer);
                lastVersionUsed = dataFromServer.version;
            }
        }
        else
            println "${name} is up to date; version $lastVersionUsed."
    }

    abstract protected void upgradeAll(dataFromServer);

    def getDataFromServer() { JSON.parse(new URL(url).text); }
}
