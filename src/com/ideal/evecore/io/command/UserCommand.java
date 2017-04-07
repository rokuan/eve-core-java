package com.ideal.evecore.io.command;

/**
 * Created by chris on 06/04/2017.
 */
public interface UserCommand {
    public static final String EVALUATE = "EVAL";
    public static final String REGISTER_RECEIVER = "RRCV";
    public static final String UNREGISTER_RECEIVER = "URCV";
    public static final String REGISTER_CONTEXT = "RCTX";
    public static final String UNREGISRER_CONTEXT = "UCTX";
    public static final String CALL_RECEIVER_METHOD = "CRMT";
    public static final String CALL_CONTEXT_METHOD = "CCMT";
    public static final String CALL_OBJECT_METHOD = "COMT";

    public static class ReceiverRequestCommand extends AbstractCommand implements UserCommand {
        private String receiverId;
        private ReceiverCommand receiverCommand;

        protected ReceiverRequestCommand() {
            super(CALL_RECEIVER_METHOD);
        }
    }

    public static class ContextRequestCommand extends AbstractCommand implements UserCommand {
        private String contextId;
        private ContextCommand contextCommand;

        protected ContextRequestCommand() {
            super(CALL_CONTEXT_METHOD);
        }
    }

    public static class RegisterReceiverCommand extends AbstractCommand implements UserCommand {
        public RegisterReceiverCommand() {
            super(REGISTER_RECEIVER);
        }
    }

    public static class UnregisterReceiverCommand extends AbstractCommand implements UserCommand {
        private String receiverId;

        protected UnregisterReceiverCommand() {
            super(UNREGISTER_RECEIVER);
        }
    }

    public static class RegisterContextCommand extends AbstractCommand implements UserCommand {
        public RegisterContextCommand() {
            super(REGISTER_CONTEXT);
        }
    }

    public static class UnregisterContextCommand extends AbstractCommand implements UserCommand {
        private String contextId;

        public UnregisterContextCommand() {
            super(UNREGISRER_CONTEXT);
        }
    }
}
