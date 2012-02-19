package kangaroo.data.prefill

import kangaroo.CourseUtils
import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional

abstract class UpdateableDataService {

    protected static String dataRoot = "https://raw.github.com/austin-college/data/master"

    @Transactional
    def upgradeIfNeeded() {

        def dataFromServer = getDataFromServer();

        if (dataFromServer.version > lastVersionUsed) {

            // Run the upgrade.
            println "\nUpgrading ${name} to version ${dataFromServer.version}..."
            println " ===> $url is at version ${dataFromServer.version}"
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
