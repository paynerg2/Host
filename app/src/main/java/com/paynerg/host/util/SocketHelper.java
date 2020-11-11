package com.paynerg.host.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.paynerg.host.R;
import com.paynerg.host.interfaces.Connectable;

public class SocketHelper <T extends Activity & Connectable> {
    private T activity;
    private Socket socket;

    public SocketHelper(T activity, Socket socket) {
        this.activity = activity;
        this.socket = socket;
    }

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!activity.isConnected()) {
                        activity.setConnected(true);
                    }
                    Log.i(activity.getTAG(), "Requesting pin...");
                    Log.i(activity.getTAG(), "from helper");
                    socket.emit(Constants.HostEvent.EVENT_REQUEST_PIN);
                }
            });
        }
    };

    public Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                Log.i(activity.getTAG(), "disconnected");
                    activity.setConnected(false);
                    Toast.makeText(activity.getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(activity.getTAG(), "Error connecting");
                    Toast.makeText(activity.getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_SHORT).show();
                    // Consider automatically attempting to reconnect some number of times.
                }
            });
        }
    };
}
