package com.android.systemui.tuner;

import android.content.Context;
import android.provider.Settings.Global;
import android.support.v14.preference.SwitchPreference;
import android.util.AttributeSet;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.systemui.R;

public class RebootSwitch extends SwitchPreference implements TunerService.Tunable {

    public RebootSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAttached() {
        super.onAttached();
        TunerService.get(getContext()).addTunable(this, getKey());
    }

    @Override
    public void onDetached() {
        TunerService.get(getContext()).removeTunable(this);
        super.onDetached();
    }

    @Override
    public void onTuningChanged(String key, String newValue) {
        setChecked(Global.getInt(getContext().getContentResolver(),
                Global.REBOOT_IN_POWER_MENU, 0) != 0);
    }

    @Override
    protected void onClick() {
        super.onClick();
        MetricsLogger.action(getContext(), MetricsEvent.ACTION_TUNER_REBOOT, isChecked());
    }

    @Override
    protected boolean persistBoolean(boolean value) {
        Global.putInt(getContext().getContentResolver(),
                    Global.REBOOT_IN_POWER_MENU, value ? 1 : 0);
        return true;
    }

}
