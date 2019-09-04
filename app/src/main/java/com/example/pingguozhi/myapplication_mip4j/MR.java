package com.example.pingguozhi.myapplication_mip4j;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.TransferSyntax;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MR extends ShortImage {
    public String patientID;
    public String studyInstanceUID;
    public String seriesInstanceUID;
    public String sopInstanceUID;

    public MR(int width, int height) {
        super(width, height);
    }

    public MR(String dcmFile) {
        DicomObject dcmObj = null;
        String  a ="";
        try (DicomInputStream din = new DicomInputStream(new File(dcmFile))) {
            dcmObj = din.readDicomObject();
            System.out.println("R");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("N");
        }
        if(dcmObj!=null){
            this.patientID = dcmObj.getString(Tag.PatientID);
            this.studyInstanceUID = dcmObj.getString(Tag.StudyInstanceUID);
            this.seriesInstanceUID = dcmObj.getString(Tag.SeriesInstanceUID);
            this.sopInstanceUID = dcmObj.getString(Tag.SOPInstanceUID);
            this.windowCenter = Integer.parseInt(dcmObj.getString(Tag.WindowCenter));
            this.windowWidth = Integer.parseInt(dcmObj.getString(Tag.WindowWidth));
            this.width = Integer.parseInt(dcmObj.getString(Tag.Columns));
            this.height = Integer.parseInt(dcmObj.getString(Tag.Rows));
            this.pixelArray = dcmObj.getShorts(Tag.PixelData);
            this.rescaleIntercept = (int) dcmObj.getDouble(Tag.RescaleIntercept);

            for (int i = 0; i < this.pixelArray.length; i++) {
                this.pixelArray[i] += this.rescaleIntercept;

            }

            flipVertically();
        }
        else
        {
            this.patientID="error,"+a;
            flipVertically();
        }

    }
    public MR(InputStream inputStream ) {
        DicomObject dcmObj = null;
        String  a ="";
        try (DicomInputStream din = new DicomInputStream(inputStream)) {
            dcmObj = din.readDicomObject();
            System.out.println("R");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("N");
        }
        if(dcmObj!=null){
            this.patientID = dcmObj.getString(Tag.PatientID);
            this.studyInstanceUID = dcmObj.getString(Tag.StudyInstanceUID);
            this.seriesInstanceUID = dcmObj.getString(Tag.SeriesInstanceUID);
            this.sopInstanceUID = dcmObj.getString(Tag.SOPInstanceUID);
            this.windowCenter = Integer.parseInt(dcmObj.getString(Tag.WindowCenter));
            this.windowWidth = Integer.parseInt(dcmObj.getString(Tag.WindowWidth));
            this.width = Integer.parseInt(dcmObj.getString(Tag.Columns));
            this.height = Integer.parseInt(dcmObj.getString(Tag.Rows));
            this.pixelArray = dcmObj.getShorts(Tag.PixelData);
            this.rescaleIntercept = (int) dcmObj.getDouble(Tag.RescaleIntercept);

            for (int i = 0; i < this.pixelArray.length; i++) {
                this.pixelArray[i] += this.rescaleIntercept;

            }

            flipVertically();
        }
        else
        {
            this.patientID="error,"+a;
            flipVertically();
        }

    }
    public void writeDICOM(String dcmFile) {
        DicomObject dcmObj = new BasicDicomObject();

        dcmObj.putString(Tag.TransferSyntaxUID, dcmObj.vrOf(Tag.TransferSyntaxUID), TransferSyntax.ImplicitVRLittleEndian.uid());
        dcmObj.putString(Tag.PatientID, dcmObj.vrOf(Tag.PatientID), this.patientID);
        dcmObj.putString(Tag.StudyInstanceUID, dcmObj.vrOf(Tag.StudyInstanceUID), this.studyInstanceUID);
        dcmObj.putString(Tag.SeriesInstanceUID, dcmObj.vrOf(Tag.SeriesInstanceUID), this.seriesInstanceUID);
        dcmObj.putString(Tag.SOPInstanceUID, dcmObj.vrOf(Tag.SOPInstanceUID), this.sopInstanceUID);

        dcmObj.putInt(Tag.BitsAllocated, dcmObj.vrOf(Tag.BitsAllocated), 16);
        dcmObj.putInt(Tag.BitsStored, dcmObj.vrOf(Tag.BitsStored), 16);
        dcmObj.putInt(Tag.HighBit, dcmObj.vrOf(Tag.HighBit), 15);
        dcmObj.putInt(Tag.SamplesPerPixel, dcmObj.vrOf(Tag.SamplesPerPixel), 1);
        dcmObj.putString(Tag.PhotometricInterpretation, dcmObj.vrOf(Tag.PhotometricInterpretation), "MONOCHROME2"); // samplesPerPixel == 3 ? "YBR_FULL_422" : "MONOCHROME2"

        dcmObj.putDouble(Tag.WindowCenter, dcmObj.vrOf(Tag.WindowCenter), this.windowCenter);
        dcmObj.putDouble(Tag.WindowWidth, dcmObj.vrOf(Tag.WindowWidth), this.windowWidth);

        dcmObj.putInt(Tag.Columns, dcmObj.vrOf(Tag.Columns), this.width);
        dcmObj.putInt(Tag.Rows, dcmObj.vrOf(Tag.Rows), this.height);
        for (int i = 0; i < this.pixelArray.length; i++) {
            this.pixelArray[i] -= this.rescaleIntercept;
        }
        dcmObj.putShorts(Tag.PixelData, dcmObj.vrOf(Tag.PixelData), this.pixelArray);
        dcmObj.putDouble(Tag.RescaleIntercept, dcmObj.vrOf(Tag.RescaleIntercept), 0);

        File f = new File(dcmFile);

        try (DicomOutputStream dos = new DicomOutputStream(new BufferedOutputStream(new FileOutputStream(f)))) {
            dos.writeDicomFile(dcmObj);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//
//        MR  mr = new MR("/Users/pingguozhi/AndroidStudioProjects/MyApplication_mip4j/app/src/main/res/raw/mr.dcm");
////        int[] int_array=new int[mr.width*mr.height];
////        for(int i=0;i<mr.width*mr.height;i++)
////        {
////            int w=i%mr.width;
////            int h=i/mr.width;
////            int_array[i]=(int)mr.getPixel(h,w);
////
////        }
//        System.out.println(mr);
//    }
}
