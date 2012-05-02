package kangaroo.data.prefill

class RooRouteDataService extends UpdateableDataService {

    static String name = "Roo route stop list"
    static String url = "${dataRoot}/rooRoute.json"
    static int lastVersionUsed = 0;

    static stopList;

    @Override
    protected void upgradeAll(dataFromServer) { stopList = dataFromServer.data.sort(); }
}
