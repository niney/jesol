package jesol

import common.controller.JSONRestfulController

class MemberController extends JSONRestfulController<Member> {

    MemberController() {
        super(Member)
    }
}
