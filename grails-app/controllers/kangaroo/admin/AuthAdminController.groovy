package kangaroo.admin

import kangaroo.api.EditKey

class AuthAdminController {

    def index = {}

    def authenticate = {

        if (params.key && EditKey.exists(params.key)) {
            flash.success = "Welcome!";
            session.isAdmin = true;
            redirect(controller: "adminJob");
        }
        else {
            flash.message = "Invalid authentication key. Please try again.";
            redirect(action: "index")
        }

    }
}
