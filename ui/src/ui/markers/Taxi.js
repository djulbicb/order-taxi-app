import React, { useState } from 'react';
import { faHome, faUserAstronaut, faTaxi } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup, Tooltip } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';

function Taxi(props) {

    const iconHTML = ReactDOMServer.renderToString(<FontAwesomeIcon icon={faTaxi}/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'marker-taxi'
    });

    return (
        <Marker  icon={customMarkerIcon} position={props.position} eventHandlers={props.eventHandlers}>  
            {/* <Tooltip direction="right" offset={[0, 0]} opacity={1} permanent>sss</Tooltip> */}
            <Popup>
              <h3>{props.id}</h3>
              {JSON.stringify(props.position)}
            </Popup>
        </Marker>
      );
  }
  
  export default Taxi;