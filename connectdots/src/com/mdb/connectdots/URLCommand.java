package com.mdb.connectdots;

import java.io.IOException;
import java.io.InputStream;

public class URLCommand {
    private String stringGET;
    private String contentTypeSuffix;
    private String contentResponse;

    public URLCommand(String stringGET) {
        this.stringGET = stringGET;
    }

    public URLCommand invoke() throws IOException {
        contentTypeSuffix = "application/javascript";
        contentResponse = "[]";
        if (stringGET.toUpperCase().startsWith("/GETBOARD")) {
            GetIndexHTML getIndexHTML = new GetIndexHTML(contentResponse).invoke();
            contentResponse = getIndexHTML.getContentResponse();
            contentTypeSuffix = getIndexHTML.getContentTypeSuffix();
        } else if (stringGET.toUpperCase().startsWith("/MOVE")) {
            int move=Integer.parseInt(
                    stringGET.toUpperCase().replace("/MOVE",""))-1;
            MainActivity.mainActivity.connectDotsGame.enterMove(move);
            contentResponse=
                    MainActivity.mainActivity.connectDotsGame.getBoard();
        }
        else if (stringGET.toUpperCase().startsWith("/THINK")) {
            MainActivity.mainActivity.connectDotsGame.run();
            contentResponse=
                    MainActivity.mainActivity.connectDotsGame.getBoard();
        } else if (stringGET.toUpperCase().startsWith("/GETJSBOARD")) {
            contentResponse=
                    MainActivity.mainActivity.connectDotsGame.getBoard();
        } else if (stringGET.toUpperCase().startsWith("/RESET")) {
            MainActivity.mainActivity.connectDotsGame.Restart();
            contentResponse=
                    MainActivity.mainActivity.connectDotsGame.getBoard();
        } else if(stringGET.toUpperCase().startsWith("/LEVEL")){
            int level=Integer.parseInt(
                    stringGET.toUpperCase().replace("/LEVEL",""))-1;
            MainActivity.connectDotsGame.SEARCHPLY=level;
            contentResponse=
                    MainActivity.mainActivity.connectDotsGame.getBoard();
        } else
        {
            GetIndexHTML getIndexHTML = new GetIndexHTML(contentResponse).invoke();
            contentResponse = getIndexHTML.getContentResponse();
            contentTypeSuffix = getIndexHTML.getContentTypeSuffix();
        }
        return this;
    }

    public String getContentTypeSuffix() {
        return contentTypeSuffix;
    }

    public String getContentResponse() {
        return contentResponse;
    }

    private class GetIndexHTML {
        private String contentResponse;
        private String contentTypeSuffix;

        public GetIndexHTML(String contentResponse) {
            this.contentResponse = contentResponse;
        }

        public String getContentTypeSuffix() {
            return contentTypeSuffix;
        }

        public String getContentResponse() {
            return contentResponse;
        }

        public GetIndexHTML invoke() throws IOException {
            int[] rawResources={R.raw.index};
            for(int idx=0;idx<rawResources.length;++idx){
                InputStream rawHtmlInputStream=
                        MainActivity.mainActivity.getResources().
                                openRawResource(rawResources[idx]);
                int rawSize=rawHtmlInputStream.available();
                byte[] rawBuffer=new byte[rawSize];
                rawHtmlInputStream.read(rawBuffer, 0, rawSize);
                contentResponse=new String(rawBuffer);
            }
            contentTypeSuffix = "text/html";
            return this;
        }
    }
}