package com.ideal.evecore.io.command;

/**
 * Created by chris on 06/04/2017.
 */
public interface ReceiverCommand {
    public static final String INIT_RECEIVER = "IRCV";
    public static final String DESTROY_RECEIVER = "DRCV";
    public static final String HANDLE_MESSAGE = "HMSG";
    public static final String GET_MAPPINGS = "GMAP";
    public static final String GET_RECEIVER_NAME = "GRNM";

    public static class InitReceiverCommand extends AbstractCommand implements ReceiverCommand {
        public InitReceiverCommand() {
            super(INIT_RECEIVER);
        }
    }

    public static class DestroyReceiverCommand extends AbstractCommand implements ReceiverCommand {
        public DestroyReceiverCommand() {
            super(DESTROY_RECEIVER);
        }
    }

    public static class HandleMessageCommand extends AbstractCommand implements ReceiverCommand {
        protected HandleMessageCommand() {
            super(HANDLE_MESSAGE);
        }
    }

    public static class GetMappingsCommand extends AbstractCommand implements ReceiverCommand {
        public GetMappingsCommand() {
            super(GET_MAPPINGS);
        }
    }

    public static class GetReceiverNameCommand extends AbstractCommand implements ReceiverCommand {
        public GetReceiverNameCommand() {
            super(GET_RECEIVER_NAME);
        }
    }
}
