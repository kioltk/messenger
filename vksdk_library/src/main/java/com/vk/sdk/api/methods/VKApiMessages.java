//
//  Copyright (c) 2014 VK.com
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy of
//  this software and associated documentation files (the "Software"), to deal in
//  the Software without restriction, including without limitation the rights to
//  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
//  the Software, and to permit persons to whom the Software is furnished to do so,
//  subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
//  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
//  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
//  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

package com.vk.sdk.api.methods;

import com.google.gson.Gson;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKParser;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiChat;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKLongPollServer;
import com.vk.sdk.api.model.VKMessagesArray;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Builds requests for API.messages part
 */
public class VKApiMessages extends VKApiBase {
    /**
     * Returns basic information about current message
     *
     * @return Request for load
     */
    public VKRequest get() {
        return get(null);
    }

    /**
     * https://vk.com/dev/messages.get
     *
     * @param params use parameters from description with VKApiConst class
     * @return Request for load
     */
    public VKRequest get(VKParameters params) {
        return prepareRequest("get", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                try {
                    return new VKMessagesArray().parse(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * https://vk.com/dev/messages.search
     *
     * @param params use parameters from description with VKApiConst class
     * @return Request for load
     */
    public VKRequest search(VKParameters params) {
        return prepareRequest("search", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiMessage>(object, VKApiMessage.class);
            }
        });
    }

    public VKRequest getChats() {
        return prepareRequest("execute", "getChats", null, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return parseDialogs(object, true, false);
            }
        });
    }

    public VKRequest getDialogs() {
        return prepareRequest("execute", "getDialogs", null, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return parseDialogs(object, true, true);
            }
        });
    }

    public VKRequest getChatInfo() {
        return prepareRequest("execute", "getChatInfo", null, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public VKApiChat createModel(JSONObject object) {
                return new VKApiChat(object);
            }
        });
    }

    private VKList<VKApiDialog> parseDialogs(JSONObject object, boolean includeChats, boolean includeConversations) {
        VKList<VKApiDialog> dialogs = new VKList<VKApiDialog>();
        try {
            JSONObject response = object.getJSONObject("response");
            VKList<VKApiUserFull> users = new VKList<VKApiUserFull>(response.getJSONObject("users"), VKApiUserFull.class);
            VKList<VKApiMessage> messages = new VKList<VKApiMessage>(response.getJSONObject("messages"), VKApiMessage.class);
            for (final VKApiMessage dialogMessage : messages) {
                if (dialogMessage.isChat()) {
                    if (includeChats) {
                        VKApiUserFull chatOwner = users.getById(dialogMessage.admin_id);
                        VKList<VKApiUserFull> chatUsers = users.getById(dialogMessage.chat_active);
                        dialogs.add(new VKApiDialog(dialogMessage, chatOwner, chatUsers));
                    }
                } else {
                    if (includeConversations) {
                        VKApiUserFull dialogOwner = users.getById(dialogMessage.user_id);
                        dialogs.add(new VKApiDialog(dialogMessage, dialogOwner));
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dialogs;
    }

    public VKRequest typing(int userId) {
        VKParameters params = new VKParameters();
        params.put("user_id", userId);
        return prepareRequest("typing", params);
    }

    public VKRequest typingChat(int chatId) {
        VKParameters params = new VKParameters();
        params.put("chat_id", chatId);
        return prepareRequest("typing", params);
    }

    public VKRequest send(VKApiMessage message) {
        VKParameters params = new VKParameters();
        params.put("message", message.body);
        if (message.isChat()) {
            params.put("chat_id", message.chat_id);
        } else {
            params.put("user_id", message.user_id);
        }
        if (message.attachments != null && !message.attachments.isEmpty())
            params.put("attachment", message.attachments.toAttachmentsString());
        params.put("guid", message.guid);
        return prepareRequest("send", params, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return object.optInt("response", -1);
            }
        });
    }

    public VKRequest sendSticker(VKApiMessage stickerMessage) {
        VKParameters params = new VKParameters();
        params.put("sticker_id", stickerMessage.body);
        if (stickerMessage.isChat()) {
            params.put("chat_id", stickerMessage.chat_id);
        } else {
            params.put("user_id", stickerMessage.user_id);
        }
        return prepareRequest("send", params, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return object.optInt("response", -1);
            }
        });
    }

    public VKRequest getChat(VKParameters vkParameters) {

        return prepareRequest("getChat", vkParameters, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKApiChat(object);
            }
        });
    }

    public VKRequest getHistory(final int peerId) {
        return getHistory(new VKParameters() {{
            if(peerId>0) {
                put("user_id", peerId);
            } else {
                put("chat_id", -peerId);

            }
        }});
    }



    public VKRequest getHistory(VKParameters params) {
        return prepareRequest("getHistory", params, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiMessage>(object, VKApiMessage.class);
            }
        });
    }

    public VKRequest getLongPollService() {
        return prepareRequest("getLongPollServer", new VKParameters(), new VKParser() {
            @Override
            public Object createModel(JSONObject response) {

                VKLongPollServer longPollServer;
                longPollServer = new Gson().fromJson(response.optJSONObject("response").toString(), VKLongPollServer.class);
                return longPollServer;
            }
        });
    }

}