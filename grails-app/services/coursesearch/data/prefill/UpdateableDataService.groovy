package coursesearch.data.prefill

import coursesearch.CourseUtils
import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional

abstract class UpdateableDataService {

    protected static String dataRoot = "https://raw.github.com/phillco/data/master"

    @Transactional
    def upgradeIfNeeded() {

        def dataFromServer = getDataFromServer();

        if (dataFromServer.version > lastVersionUsed) {

            // Run the upgrade.
            println "Upgrading ${name} to version ${dataFromServer.version}..."
            CourseUtils.runAndTime("${name} updated") {
                upgradeAll(dataFromServer);
                lastVersionUsed = dataFromServer.version;
                println "${name} is now at version ${lastVersionUsed}"
            }
        }
        else
            println "${name} is up to date; version $lastVersionUsed."
    }

    abstract protected void upgradeAll(dataFromServer)

    ;

    def getDataFromServer() { JSON.parse(new URL(url).text); }
}
