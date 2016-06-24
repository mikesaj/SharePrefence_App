package com.appsrus.mikesaj.ui_app_assignment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;


/*
* This is the main activity for the mobile app
* i.e. the first class to be accessed by the mobile O.S
* */
public class MainActivity extends AppCompatActivity {

    // Save button Object declaration
    Button saveBtn;

    // Edittext Object Declaration for names
    EditText nameLabel;

    // CheckBox Object declaration
    CheckBox checkBox;

    // Spinner Object Declaration
    Spinner eyeColorDropDown;
    
    // RadioGroup of radio object
    RadioGroup shirtSizeGroupRadio;

    // Slider Object Declaration
    SeekBar pantSizeSlider;
    SeekBar shirtSizeSlider;
    SeekBar shoeSizeSlider;

    // TextView declaration for the seeker objects
    TextView seekBarPant,seekBarShirt,seekBarShoe;

    // Calendar Object Declaration
    static TextView birthCalendarView;

    // Selected Radio button Object declaration
    RadioButton selectedRadioButton;

    // SharedPreferences SharedPreferences declaration
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    private static final String MyPREFERENCES = "MyPrefs" ;




    // This method creates the activity and initializes its objects
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the SharedPreferences objects
        settings = getSharedPreferences(MyPREFERENCES, 0);
        editor = settings.edit();


        // Initialize Activity Objects
        initializeObjects();

        // Initialize Activity's Previous State and wrapping it into a try-catch structure to handle errors
        try{
            initializeState();
        }catch(Exception e){}



        // This sets a click Listener to the 'save' button in the activity
        saveBtn.setOnClickListener(new View.OnClickListener() {

            // This method fires when a click is made on the 'save' button
            @Override
            public void onClick(View v) {

                // This method calls the saveEvent method to save the state of the activity
                saveEvent();

            }
        });


        // Initializes sliders information label of the values/progress of its slider
        seekBarPant.setText(pantSizeSlider.getProgress() + "/" + pantSizeSlider.getMax());
        seekBarShirt.setText(shirtSizeSlider.getProgress() + "/" + shirtSizeSlider.getMax());

        // Setting minimun value for the shoe size seeker
        final int minValue = 4;
        
        //setting text for the shoeSize label
        seekBarShoe.setText((shoeSizeSlider.getProgress() + minValue) + "/" + (shoeSizeSlider.getMax() ));

        // Setting a change listener on the pantSize seekbar
        pantSizeSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    // Change listener on the pantSize seekbar
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        progress = progressValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        seekBarPant.setText(progress + "/" + seekBar.getMax());
                    }
                });

        // Setting a change listener on the shirtSize seekbar
        shirtSizeSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    // Change listener on the shirtSize seekbar
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        // Updates the progress value
                        progress = progressValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    // Change listener when the user concludes a seekbar drag
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        // Display the value in textview
                        seekBarShirt.setText(progress + "/" + seekBar.getMax());
                    }
                });

        // Setting a change listener on the shoeSize seekbar
        shoeSizeSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    // Change listener on the shoeSize seekbar
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        // Updates the progress value
                        progress = progressValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    // Change listener when the user concludes a seekbar drag
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        if(progress<4){progress = minValue;}
                        seekBarShoe.setText((progress) + "/" + (seekBar.getMax() ));
                    }
                });

        // END OF Sliders block
    }

    // This method is fired to load data to the activity objetcs when the application is launched
    private void initializeState(){

        // This Loads data from the sharedpreference storage file and sets the activity objects

        int selectedRadioId    =  settings.getInt("selectedRadioId", 0);
        String name            =  settings.getString("name", "");
        int eyeColor           =  settings.getInt("eyeColor", 0);

        boolean checkBoxValue  = Boolean.parseBoolean(settings.getString("checkBoxValue", "false"));

        int pantSlider         =  settings.getInt( "pantSlider",  0);
        int shirtSlider        =  settings.getInt( "shirtSlider", 0);
        int shoeSlider         =  settings.getInt( "shoeSlider",  0);
        String birthDate       =  settings.getString("birthDate", "06/30/2016");

        // Set Values
        // --selectedRadioButton    =  (RadioButton) findViewById(selectedRadioId);

        // This sets the objects with the value found in the sharedpreference storage
        shirtSizeGroupRadio.check(selectedRadioId);
        nameLabel.setText(name);
        eyeColorDropDown.setSelection(eyeColor);
        checkBox.setChecked(checkBoxValue);
        pantSizeSlider.setProgress(pantSlider);
        shirtSizeSlider.setProgress(shirtSlider);
        shoeSizeSlider.setProgress(shoeSlider);

        birthCalendarView.setText(birthDate);

    }

    // This method is fired to save data to the sharedpreference file when the save button is clicked
    private void saveEvent(){


        int selectedRadioId = shirtSizeGroupRadio.getCheckedRadioButtonId();


        // This gets data from the activity objects and saves them into variables
        String name            =  nameLabel.getText().toString();
        int eyeColor           =  eyeColorDropDown.getSelectedItemPosition();

        boolean checkBoxValue  =  checkBox.isChecked();

        int pantSlider         =  pantSizeSlider.getProgress();
        int shirtSlider        =  shirtSizeSlider.getProgress();
        int shoeSlider         =  shoeSizeSlider.getProgress();
        String birthDate       =  birthCalendarView.getText().toString();



        // Saving data into SharedPreference
        editor.putInt("selectedRadioId",   selectedRadioId);
        editor.putString("name",           name);
        editor.putInt("eyeColor",          eyeColor);
        editor.putString("checkBoxValue",  checkBoxValue+"");
        editor.putInt("pantSlider",        pantSlider);
        editor.putInt("shirtSlider",       shirtSlider);
        editor.putInt("shoeSlider",        shoeSlider);
        editor.putString("birthDate",      birthDate);

        // Commit saves the collected data into the preference data file
        editor.commit();

        // Toast message to alert the user of a data save event
        Toast.makeText(MainActivity.this,"Saved!\nThanks",Toast.LENGTH_LONG).show();

    }
    // This method initializes this activities objects, methods and associated them with their respective ids
    private void initializeObjects(){

        // Initializing activity objects with their ids
        nameLabel           = (EditText)  findViewById(R.id.name);
        eyeColorDropDown    = (Spinner)   findViewById(R.id.eyeColor);

        checkBox            = (CheckBox)  findViewById(R.id.checkBox);

        pantSizeSlider      = (SeekBar)   findViewById(R.id.pantSize);
        shirtSizeSlider     = (SeekBar)   findViewById(R.id.shirtSize);
        shoeSizeSlider      = (SeekBar)   findViewById(R.id.shoeSize);


        seekBarPant         = (TextView)  findViewById(R.id.pantSizeMeter);
        seekBarShirt        = (TextView)  findViewById(R.id.shirtSizeMeter);
        seekBarShoe         = (TextView)  findViewById(R.id.shoeSizeMeter);

        birthCalendarView   = (TextView)  findViewById(R.id.dateofBirth);
        shirtSizeGroupRadio = (RadioGroup)findViewById(R.id.shirtSizeGroupRadio);

        saveBtn             = (Button)    findViewById(R.id.save);
    }

    // Dialog date prompt method for the activities calendar function
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // This is a subclass for the date fragments and its methods
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            // Get current date and save into variables
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        // Set the date format to be displayed for the calendar label
        public void onDateSet(DatePicker view, int year, int month, int day) {
            birthCalendarView.setText((month + 1) + "/" + day + "/" + year);
        }
    }


}
