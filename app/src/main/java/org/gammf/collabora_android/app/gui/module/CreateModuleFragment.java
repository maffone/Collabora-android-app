package org.gammf.collabora_android.app.gui.module;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateModuleFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";

    private String username;
    private String collaborationId;
    private String state;
    private ListView previousModulesList;
    private ArrayList<CollaborationComponentInfo> moduleItems;
    private ArrayList<String> previousModulesSelected ;
    private EditText txtContentModule;

    public CreateModuleFragment() {
        setHasOptionsMenu(false);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId collaboration id where the module will be added
     * @return A new instance of fragment CreateModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateModuleFragment newInstance(String username, String collaborationId) {
        CreateModuleFragment fragment = new CreateModuleFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_USERNAME, username);
        arg.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if(getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_module, container, false);
        moduleItems = new ArrayList<>();
        previousModulesSelected = new ArrayList<>();
        initializeGuiComponent(rootView);
        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        txtContentModule = rootView.findViewById(R.id.txtNewModuleContent);
        previousModulesList = rootView.findViewById(R.id.listViewPModules);
        final StateSpinnerManager spinnerManager = new StateSpinnerManager(StateSpinnerManager.NO_STATE, rootView, R.id.spinnerNewModuleState, CollaborationType.PROJECT);
        spinnerManager.addObserver(new Observer<String>() {
            @Override
            public void notify(String newState) {
                state = newState;
            }
        });

        FloatingActionButton btnAddModule = rootView.findViewById(R.id.btnAddModule);
        btnAddModule.setOnClickListener(this);
        Button btnAddPModules = rootView.findViewById(R.id.btnAddPModules);
        btnAddPModules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> listItems = new ArrayList<>();
                final List<Module> allModules = new ArrayList<>();
                final List<Integer> mSelectedItems = new ArrayList<>();
                try {
                    Collaboration collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
                    allModules.addAll(((ConcreteProject)collaboration).getAllModules());
                    for (Module module : allModules) {
                        listItems.add(module.getDescription());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                final CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select previous modules")
                        .setMultiChoiceItems(charSequenceItems, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            mSelectedItems.add(which);
                                        } else if (mSelectedItems.contains(which)) {
                                            mSelectedItems.remove(Integer.valueOf(which));
                                        }
                                    }
                                })
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                moduleItems.clear();
                                previousModulesSelected.clear();
                                for (int position: mSelectedItems) {
                                    previousModulesSelected.add(allModules.get(position).getId());
                                    moduleItems.add(new CollaborationComponentInfo(allModules.get(position).getId(), allModules.get(position).getDescription(), CollaborationComponentType.NOTE));
                                }
                                final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, moduleItems);
                                previousModulesList.setAdapter(noteListAdapter);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void addModule(final String content, final String stateSelected) {
        final Module module = new ConcreteModule(null, content, stateSelected);
        final ModuleUpdateMessage message = new ConcreteModuleUpdateMessage(
                username, module, UpdateMessageType.CREATION, collaborationId);
        new SendMessageToServerTask(getContext()).execute(message);
    }

    @Override
    public void onClick(View view) {
        String insertedModuleName = txtContentModule.getText().toString();
        if (insertedModuleName.equals("")) {
            txtContentModule.setError(getResources().getString(R.string.fieldempty));
        } else {
            addModule(insertedModuleName, this.state);
        }
    }
}
