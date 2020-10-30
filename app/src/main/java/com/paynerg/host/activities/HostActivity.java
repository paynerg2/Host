package com.paynerg.host.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.paynerg.host.R;
import com.paynerg.host.adapters.MenuItemAdapter;
import com.paynerg.host.models.MenuItem;
import com.paynerg.host.util.SwipeToDeleteCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.paynerg.host.util.Constants.DEV_SOCKET_URL;
import static com.paynerg.host.util.Constants.HostEvent;

public class HostActivity extends AppCompatActivity {

    public static final String TAG = "HostActivity";

    private RecyclerView recyclerViewMenu;
    private TextView textViewPin;

    private Socket socket;
    private List<MenuItem> menu;
    private RecyclerView.Adapter adapter;

    private boolean isConnected;
    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        menu = new ArrayList<>();

        textViewPin = findViewById(R.id.text_view_pin);
        recyclerViewMenu = findViewById(R.id.recycler_view_menu);
        adapter = new MenuItemAdapter(this, menu);

        setUpRecyclerView();

        try {
            socket = IO.socket(DEV_SOCKET_URL);

            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            socket.on(HostEvent.EVENT_GUEST_HAS_ORDER, onGuestHasOrder);
            socket.on(HostEvent.EVENT_PIN_RECEIVED, onPinReceived);

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        //startSignIn();

        // Make menu available on the server
//        JSONObject menuJSON = null;
//        try {
//            menuJSON = convertToJSON(menu);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if(menuJSON != null){
//            socket.emit(HostEvent.EVENT_SEND_MENU, menuJSON);
//        } else {
//            // something went wrong
//        }

    }

    private void setUpRecyclerView() {
        recyclerViewMenu.setHasFixedSize(false);
        recyclerViewMenu.setAdapter(adapter);
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new SwipeToDeleteCallback((MenuItemAdapter) adapter));
        itemTouchHelper.attachToRecyclerView(recyclerViewMenu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.off(HostEvent.EVENT_GUEST_HAS_ORDER, onGuestHasOrder);
        socket.off(HostEvent.EVENT_PIN_RECEIVED, onPinReceived);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        isConnected = true;
                    }
                    Log.i(TAG, "Requesting pin...");
                    socket.emit(HostEvent.EVENT_REQUEST_PIN);
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "disconnected");
                    isConnected = false;
                    Toast.makeText(getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    Toast.makeText(getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_SHORT).show();
                    // Consider automatically attempting to reconnect some number of times.
                }
            });
        }
    };

    private Emitter.Listener onPinReceived = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "Pin received");
                    String receivedPin = (String) args[0];
                    if (pin != receivedPin && !receivedPin.isEmpty()) {
                        pin = receivedPin;
                    }
                    textViewPin.setText(pin);
                }
            });
        }
    };

    private Emitter.Listener onGuestHasOrder = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "Guest order received.");
                    JSONObject data = (JSONObject) args[0];
                    addOrder(data);
                }
            });
        }
    };

    private void addOrder(JSONObject data) {
        try {
            Log.i(TAG, "in try in addOrder");
            MenuItem item = getMenuItemFromJSON(data);
            menu.add(item);
            for (MenuItem m : menu) {
                Log.i(TAG, m.getItemName());
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (menu.size() == 1) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemInserted(menu.size() - 1);
        }

    }

    private MenuItem getMenuItemFromJSON(JSONObject obj) throws JSONException {
        String itemName = obj.getString("itemName");
        String description = obj.getString("description");
        return new MenuItem(itemName, description);
    }

    private JSONObject convertToJSON(List<MenuItem> menu) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        for (MenuItem m : menu) {
            arr.put(m.toJSON());
        }
        obj.put("menu", arr);
        return obj;
    }
}