package org.tinygroup.fileresolver.util;

import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class FileResolverLoadUtil {


    private static void getFileResolverConfig(FileResolver fileResolver,
                                               String applicationConfig) {
        XmlStringParser parser = new XmlStringParser();
        XmlNode root = parser.parse(applicationConfig).getRoot();
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(root);
        XmlNode appConfig = filter
                .findNode("/application/file-resolver-configuration");
        fileResolver.config(appConfig, null);

    }

    public static void loadFileResolverConfig(FileResolver fileResolver,
                                               String applicationConfig) {
        if(fileResolver.getScanningPaths()==null
                || fileResolver.getScanningPaths().size()==0){
            getFileResolverConfig(fileResolver,applicationConfig);
        }
    }


}
