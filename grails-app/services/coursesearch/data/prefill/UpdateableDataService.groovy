package coursesearch.data.prefill

import coursesearch.CourseUtils
import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional

abstract class UpdateableDataService {

    protected static String dataRoot = "https://raw.github.com/austin-college/data/master"

    @Transactional
    def upgradeIfNeeded() {

        def dataFromServer = getDataFromServer();

        if (dataFromServer.version > lastVersionUsed) {

            println "$url ==> ${dataFromServer.version}"

            // Run the upgrade.
            println "Upgrading ${name} to version ${dataFromServer.version}..."
            CourseUtils.runAndTime("${name} updated") {
                upgradeAll(dataFromServer);
                lastVersionUsed = dataFromServer.version;
            }
        }
        else
            println "${name} is up to date; version $lastVersionUsed."
    }

    abstract protected void upgradeAll(dataFromServer)

    def getDataFromServer() { JSON.parse(new URL(url).text); }
}
