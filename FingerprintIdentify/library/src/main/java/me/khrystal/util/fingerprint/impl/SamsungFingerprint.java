package me.khrystal.util.fingerprint.impl;

import android.app.Activity;

import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

import me.khrystal.util.fingerprint.base.BaseFingerprint;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/3
 * update time:
 * email: 723526676@qq.com
 */

public class SamsungFingerprint extends BaseFingerprint {

    private int mResultCode = -1;
    private SpassFingerprint mSpassFingerprint;

    public SamsungFingerprint(Activity activity, FingerprintIdentifyExceptionListener expectionListener) {
        super(activity, expectionListener);
        try {
            Spass spass = new Spass();
            spass.initialize(mActivity);
            mSpassFingerprint = new SpassFingerprint(activity);
            setHardwareEnable(spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT));
            setRegisteredFingerprint(mSpassFingerprint.hasRegisteredFinger());
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSpassFingerprint.startIdentify(new SpassFingerprint.IdentifyListener() {
                        @Override
                        public void onFinished(int i) {
                            mResultCode = i;
                        }

                        @Override
                        public void onReady() {

                        }

                        @Override
                        public void onStarted() {

                        }

                        @Override
                        public void onCompleted() {
                            switch (mResultCode) {
                                case  SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS:
                                case  SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS:
                                    onSucceed();
                                    break;
                                case SpassFingerprint.STATUS_TIMEOUT_FAILED:
                                case SpassFingerprint.STATUS_BUTTON_PRESSED:
                                case SpassFingerprint.STATUS_QUALITY_FAILED:
                                case SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE:
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_FAILED:
                                    onNotMatch();
                                    break;

                                default:
                                    onFailed();
                                    break;

                            }
                        }
                    });
                } catch (Throwable e) {
                    onCatchException(e);
                    onFailed();
                }
            }
        });
    }

    @Override
    protected void doCancelIdentify() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSpassFingerprint != null) {
                        mSpassFingerprint.cancelIdentify();
                    }
                } catch (Throwable e) {
                    onCatchException(e);
                }
            }
        });
    }
}
