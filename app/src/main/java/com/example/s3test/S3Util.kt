package com.example.s3test

import android.content.Context
import android.graphics.Region
import android.icu.util.TimeZone.getRegion
import android.text.TextUtils
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.metrics.AwsSdkMetrics.getRegion
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region.getRegion
import com.amazonaws.regions.RegionUtils.getRegion
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import org.jetbrains.annotations.Nullable
import java.io.File


class S3Util {

    AWSCredentials awsCredentials = new BasicAWSCredentials("AccessKey", "Secret Key");	// IAM 생성하며 받은 것 입력
    AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

    TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getActivity().getApplicationContext()).build();
    TransferNetworkLossHandler.getInstance(getActivity().getApplicationContext());

    TransferObserver uploadObserver = transferUtility.upload("talent-house-app/photo", fileName, file);	// (bucket api, file이름, file객체)

    uploadObserver.setTransferListener(new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {
            if (state == TransferState.COMPLETED) {
                // Handle a completed upload
            }
        }
        @Override
        public void onProgressChanged(int id, long current, long total) {
            int done = (int) (((double) current / total) * 100.0);
            Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done");
        }
        @Override
        public void onError(int id, Exception ex) {
            Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:" + ex.toString());
        }
    }

}