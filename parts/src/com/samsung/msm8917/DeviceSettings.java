/*
 * Copyright (C) 2018 The Asus-SDM660 Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.samsung.msm8917;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import com.samsung.msm8917.kcal.KCalSettingsActivity;
import com.samsung.msm8917.preferences.CustomSeekBarPreference;
import com.samsung.msm8917.preferences.SecureSettingListPreference;
import com.samsung.msm8917.preferences.SecureSettingSwitchPreference;
//import com.samsung.msm8917.preferences.VibratorStrengthPreference;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    final static String PREF_TORCH_BRIGHTNESS = "torch_brightness";
    private final static String TORCH_1_BRIGHTNESS_PATH = "/sys/class/leds/S2MU005_FLASH_LED/brightness";
    private final static String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom," +
            "spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_1/max_brightness";

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    private static final String CATEGORY_DISPLAY = "display";
    private static final String PREF_DEVICE_KCAL = "device_kcal";

    final static String PREF_HEADPHONE_GAIN = "headphone_gain";
    private static final String HEADPHONE_GAIN_PATH = "/sys/kernel/sound_control/headphone_gain";
    final static String PREF_MICROPHONE_GAIN = "microphone_gain";
    private static final String MICROPHONE_GAIN_PATH = "/sys/kernel/sound_control/mic_gain";
    public static final String PREF_SPEAKER_GAIN = "speaker_gain";
    public static final String SPEAKER_GAIN_PATH = "/sys/kernel/sound_control/speaker_gain";
    
    
    public static final String PREF_BACKLIGHT_DIMMER = "backlight_dimmer";
    public static final String BACKLIGHT_DIMMER_PATH = "/sys/module/mdss_fb/parameters/backlight_dimmer";



    private CustomSeekBarPreference mTorchBrightness;
    //private VibratorStrengthPreference mVibratorStrength;
    private CustomSeekBarPreference mHeadphoneGain;
    private CustomSeekBarPreference mMicrophoneGain;
    private CustomSeekBarPreference mSpeakerGain;


    private Preference mKcal;

//    private SecureSettingSwitchPreference mBacklightDimmer;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_samsung_msm8917, rootKey);

        String device = FileUtils.getStringProp("ro.build.product", "unknown");

		//Torch
       // mTorchBrightness = (CustomSeekBarPreference) findPreference(PREF_TORCH_BRIGHTNESS);
        //mTorchBrightness.setEnabled(FileUtils.fileWritable(TORCH_1_BRIGHTNESS_PATH));
        //mTorchBrightness.setOnPreferenceChangeListener(this);

        //PreferenceCategory displayCategory = (PreferenceCategory) findPreference(CATEGORY_DISPLAY);
		//
		
		//audio
        mHeadphoneGain = (CustomSeekBarPreference) findPreference(PREF_HEADPHONE_GAIN);
        mHeadphoneGain.setOnPreferenceChangeListener(this);

        mMicrophoneGain = (CustomSeekBarPreference) findPreference(PREF_MICROPHONE_GAIN);
        mMicrophoneGain.setOnPreferenceChangeListener(this);

        mSpeakerGain = (CustomSeekBarPreference) findPreference(PREF_SPEAKER_GAIN);
        mSpeakerGain.setOnPreferenceChangeListener(this);
		//
			
       
        //vibrator
        //mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        //if (mVibratorStrength != null) {
        //    mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        //}
		//

        //if (FileUtils.fileWritable(BACKLIGHT_DIMMER_PATH)) {
        //    mBacklightDimmer = (SecureSettingSwitchPreference) findPreference(PREF_BACKLIGHT_DIMMER);
        //    mBacklightDimmer.setEnabled(BacklightDimmer.isSupported());
        //    mBacklightDimmer.setChecked(BacklightDimmer.isCurrentlyEnabled(this.getContext()));
        //    mBacklightDimmer.setOnPreferenceChangeListener(new BacklightDimmer(getContext()));
        //} else {
        //    getPreferenceScreen().removePreference(findPreference(PREF_BACKLIGHT_DIMMER));
        //}


		//Kcal
	    mKcal = findPreference(PREF_DEVICE_KCAL);

        mKcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
            startActivity(intent);
            return true;
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {
            //case PREF_TORCH_BRIGHTNESS:
            //   FileUtils.setValue(TORCH_1_BRIGHTNESS_PATH, (int) value);
            //    break;

            case PREF_HEADPHONE_GAIN:
                FileUtils.setValue(HEADPHONE_GAIN_PATH, value + " " + value);
                break;

            case PREF_MICROPHONE_GAIN:
                FileUtils.setValue(MICROPHONE_GAIN_PATH, (int) value);
                break;
            case PREF_SPEAKER_GAIN:
            	FileUtils.setValue(SPEAKER_GAIN_PATH, (int) value);
            	break;

            default:
                break;
        }
        return true;
    }

    private boolean isAppNotInstalled(String uri) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
