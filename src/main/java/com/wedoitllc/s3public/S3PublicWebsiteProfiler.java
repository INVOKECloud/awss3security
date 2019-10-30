/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wedoitllc.s3public;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder;
import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;
import com.amazonaws.services.route53.model.ResourceRecordSet;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Prasad
 */
public class S3PublicWebsiteProfiler {

    public static void main(String[] args) {
        
        AmazonRoute53 r53 = AmazonRoute53ClientBuilder.standard().withRegion(Regions.US_EAST_1).
                withCredentials(new InstanceProfileCredentialsProvider(false)).build();

        List<HostedZone> hostedZones = r53.listHostedZones().getHostedZones();

        if (hostedZones != null && !hostedZones.isEmpty()) {
            Set<String> bucketNames = filterS3OnlyHostedZones(r53, hostedZones);
            if (bucketNames != null && !bucketNames.isEmpty()) {
                System.out.println("Following are the R53 domains with NO S3 bucket found");
                bucketNames.forEach((bucketName) -> {
                    System.out.println(bucketName);
                });

            } else {
                System.out.println("Couldn't find any domains which are NOT associated with S3 buckets. ");
            }
        }
    }

    private static Set<String> filterS3OnlyHostedZones(AmazonRoute53 r53, List<HostedZone> hostedZones) {
        Set<String> bucketNames = new HashSet<>();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).
                withCredentials(new InstanceProfileCredentialsProvider(false)).build();

        if (hostedZones != null && !hostedZones.isEmpty()) {
            for (HostedZone hz : hostedZones) {
                ListResourceRecordSetsRequest lrrsreq = new ListResourceRecordSetsRequest(hz.getId());
                List<ResourceRecordSet> recordSetList = r53.listResourceRecordSets(lrrsreq).getResourceRecordSets();
                if (recordSetList != null && !recordSetList.isEmpty()) {
                    //Check if recordset is of type S3 endpoint
                    recordSetList.stream().filter((resRecSet) -> (resRecSet != null && resRecSet.getAliasTarget() != null)).map((resRecSet) -> resRecSet.getName()).filter((recordSetName) -> (recordSetName != null && !"".equals(recordSetName))).map((recordSetName) -> {
                        if (recordSetName.endsWith(".")) {
                            recordSetName = recordSetName.substring(0, recordSetName.length() - 1);
                        }
                        return recordSetName;
                    }).filter((recordSetName) -> (!s3.doesBucketExistV2(recordSetName))).forEachOrdered((recordSetName) -> {
                        bucketNames.add(recordSetName);
                    });
                }
            }
        }
        return bucketNames;
    }
}
