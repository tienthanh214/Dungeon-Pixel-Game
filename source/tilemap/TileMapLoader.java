package Game.StrategyDemo.source.tilemap;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Game.StrategyDemo.source.utils.BoundingBox;

public class TileMapLoader {
    /**
     * 
     * @param path
     * @param factor
     * @return whole Map from tmx file path, image scale by factor
     */
    static public Map load(String path, double factor) {
        Map map = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            doc.getDocumentElement().normalize();
            Element mapInfo = doc.getDocumentElement();
            int mapWidth = Integer.parseInt(mapInfo.getAttribute("width"));
            int mapHeight = Integer.parseInt(mapInfo.getAttribute("height"));
            int mapTileWidth = Integer.parseInt(mapInfo.getAttribute("tilewidth"));
            int mapTileHeight = Integer.parseInt(mapInfo.getAttribute("tileheight"));
            Element tilesetInfo = ((Element)doc.getElementsByTagName("tileset").item(0));
            String imgPath = tilesetInfo.getAttribute("name");
            int tileColumns = Integer.parseInt(tilesetInfo.getAttribute("columns"));
            imgPath = "../../assets/image/" + imgPath + ".png";
            map = new Map(imgPath, mapWidth, mapHeight, mapTileWidth, mapTileHeight, tileColumns, factor);
            
            // --------------- load animation ----------------------
            NodeList animationList = doc.getElementsByTagName("animation");
            for (int i = 0; i < animationList.getLength(); ++i) {
                Element element = (Element)animationList.item(i);
                NodeList durationList = element.getElementsByTagName("frame");
                int grid = Integer.parseInt(element.getParentNode().getAttributes().item(0).getTextContent());
                Integer[] frameList = new Integer[durationList.getLength()];
                int duration = 0;
                for (int j = 0; j < durationList.getLength(); ++j) {
                    Element element2 = (Element)durationList.item(j);
                    frameList[j] = Integer.parseInt(element2.getAttribute("tileid"));
                    duration += Integer.parseInt(element2.getAttribute("duration"));
                }
                map.addAnimation(grid, frameList, duration);
            }
            
            // --------------- load bounding box -----------------
            NodeList boxList = doc.getElementsByTagName("objectgroup");
            for (int i = 0; i < boxList.getLength(); ++i) {
                Element element = (Element)boxList.item(i);
                NodeList boundList = element.getElementsByTagName("object");
                int grid = Integer.parseInt(element.getParentNode().getAttributes().item(0).getTextContent());
                BoundingBox[] boundingBoxList = new BoundingBox[boundList.getLength()];
                for (int j = 0; j < boundList.getLength(); ++j) {
                    Element element2 = (Element)boundList.item(j);
                    double x = Double.parseDouble(element2.getAttribute("x")); 
                    double y = Double.parseDouble(element2.getAttribute("y")); 
                    double width = Double.parseDouble(element2.getAttribute("width")); 
                    double height = Double.parseDouble(element2.getAttribute("height")); 
                    boundingBoxList[j] = new BoundingBox(x * factor, y * factor, width * factor, height * factor);
                }
                map.addBoundingBox(grid, boundingBoxList);
            }


            // --------------- load layer --------------------------
            NodeList layerList = doc.getElementsByTagName("layer");
            for (int i = 0; i < layerList.getLength(); ++i) {
                Element element = (Element)layerList.item(i);
                String data = element.getElementsByTagName("data").item(0).getTextContent();
                map.addLayer(data, i == 0 ? true : false);
            }
            System.out.println("loaded full map");
            // for reduce memory (map no need to store because all tiles store)
            map.resetAnimation();
            map.resetBoundingBox();
        
        } catch (Exception ex) {
            System.out.println("Can't read tmx file map");
            ex.printStackTrace();
        }

        return map;
    }

    static public Map load(String path) {
        return load(path, 1);
    }

}
