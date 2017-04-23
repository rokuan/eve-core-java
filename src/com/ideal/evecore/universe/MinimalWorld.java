package com.ideal.evecore.universe;

import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.universe.World;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Predicate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christophe on 22/04/2017.
 */
public class MinimalWorld implements World {
    private final Map<String, Receiver> receivers = new LinkedHashMap<String, Receiver>();

    @Override
    public void registerReceiver(Receiver r) {
        String receiverName = r.getReceiverName();
        Receiver oldReceiver = receivers.get(receiverName);
        if (oldReceiver != null) {
            oldReceiver.destroyReceiver();
        }
        receivers.put(receiverName, r);
        r.initReceiver();
    }

    @Override
    public void unregisterReceiver(Receiver r) {
        String receiverName = r.getReceiverName();
        Receiver existingReceiver = receivers.remove(receiverName);

        if (existingReceiver != null) {
            existingReceiver.destroyReceiver();
            // TODO: what to do when those are different receivers
        }
    }

    @Override
    public Option<Receiver> findReceiver(EveStructuredObject o) {
        for (Receiver r: receivers.values()) {
            Mapping<ValueMatcher> matchers = r.getMappings();
            boolean found = true;
            for (final Map.Entry<String, ValueMatcher> entry: matchers.entrySet()) {
                Predicate<EveObject> valueMatches = new Predicate<EveObject>() {
                    @Override
                    public boolean matches(EveObject eveObject) {
                        return entry.getValue().matches(eveObject);
                    }
                };
                if (!o.get(entry.getKey()).exists(valueMatches)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return new Option.Some(r);
            }
        }
        return Option.empty();
    }
}
