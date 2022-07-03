import Coordinate from "./Coordinate";
import SampleLayer from "./dto/SampleLayer";
import SampleObject from "./dto/SampleObjects";
import SampleSize from "./dto/SampleSize";

class SampleRequest {
    coordinate: Coordinate;
    objects: SampleObject[];
    size: SampleSize;
    layer: SampleLayer;
}