package example.group;

import com.vmware.xenon.common.Operation;
import com.vmware.xenon.common.ServiceDocument;
import com.vmware.xenon.common.ServiceDocumentDescription.PropertyUsageOption;
import com.vmware.xenon.common.StatefulService;
import com.vmware.xenon.common.Utils;

/**
 * Created by menonv on 8/18/2017.
 */
public class PersonService  extends StatefulService {

    public static final String FACTORY_LINK = "/quickstart/people";

    public static class Person extends ServiceDocument {

        @UsageOption(option = PropertyUsageOption.REQUIRED)
        public String name;

        @UsageOption(option = PropertyUsageOption.AUTO_MERGE_IF_NOT_NULL)
        @UsageOption(option = PropertyUsageOption.LINK)
        public String facebookLink;
    }


    public PersonService() {
        super(Person.class);
        toggleOption(ServiceOption.PERSISTENCE, true);
        toggleOption(ServiceOption.REPLICATION, true);
        toggleOption(ServiceOption.INSTRUMENTATION, true);
        toggleOption(ServiceOption.OWNER_SELECTION, true);
    }


    @Override
    public void handleCreate(Operation startPost) {
        Person s = startPost.getBody(Person.class);
        Utils.validateState(getStateDescription(), s);
        startPost.complete();
    }


    @Override
    public void handlePut(Operation put) {
        Person newState = getBody(put);
        Utils.validateState(getStateDescription(), newState);
        setState(put, newState);
        put.complete();
    }

    @Override
    public void handlePatch(Operation patch) {
        Person state = getState(patch);
        Person patchBody = getBody(patch);
        Utils.mergeWithState(getStateDescription(), state, patchBody);
        patch.setBody(state);
        patch.complete();
    }
}
