
import React, { useEffect, useState } from "react";
import { Marker, Polyline, Popup, Tooltip, useMapEvents } from "react-leaflet";
import MapLabel from "../markers/MapLabel";
const GridLayer = (props) => {

    const polyline = [];
    const gps_labels = [];

    const redBlueOptions = { fillColor: 'red' }

    const y_start = 52.70;
    const x_start = 13.0;

    const x_increment = 0.1;
    const y_increment = 0.05;

    const x_iterations = 9;
    const y_iterations = 9;

    for(let i=0; i<x_iterations; i++) {
        polyline.push([[y_start, x_start + x_increment*i], [y_start - x_increment*(y_iterations-5),  x_start + x_increment*i]]);
    }
    for(let i=0; i<y_iterations; i++) {
        polyline.push([[y_start - y_increment*i, x_start], [y_start - y_increment*i,  x_start + y_increment * (x_iterations + y_iterations-2)]]);
    }

    for(let i=0; i<x_iterations; i++) {
        for(let j=0; j<y_iterations; j++) {
            {/* position={[y_start, x_start]} */}
            gps_labels.push([ y_start - y_increment*i, x_start + x_increment*j])
        }
    }

    const map = useMapEvents({
        click(e) {
            props.onClick && props.onClick()
            console.log("Clicked")
        },
        dragend(e) {              
            console.log("Move")                  
           props.onMoveEnd && props.onMoveEnd(e.target.getCenter());
        },            
    })
    
    return (
        <div eventhandlers={map}>

        {gps_labels.map(coordinate => (<MapLabel key={coordinate} position={coordinate}></MapLabel>))}
        <Polyline pathOptions={redBlueOptions} positions={polyline} />
        </div>
    )
}
export default GridLayer;