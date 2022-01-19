package io.github.zrdzn.bot.xorbot.log;

public enum LogAction {

    MEMBER_JOIN("Member joined"),
    MEMBER_LEAVE("Member leaved"),
    MESSAGE_DELETE("Message deleted"),
    MESSAGE_EDIT("Message edited"),
    MEMBER_WARN_ADD("Member warned"),
    MEMBER_WARN_REMOVE("Member unwarned"),
    MEMBER_MUTE("Member muted"),
    MEMBER_UNMUTE("Member unmuted"),
    MEMBER_KICK("Member kicked"),
    MEMBER_BAN("Member banned"),
    MEMBER_UNBAN("Member unbanned");

    private final String description;

    LogAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}