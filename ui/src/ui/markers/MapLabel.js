import React, { useState } from 'react';
import { faHome, faUserAstronaut, faTaxi } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup, Tooltip } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';

function MapLabel(props) {
    const iconHTML = ReactDOMServer.renderToString(<span/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'mapLabelMarker'
    });

    const roundDown = (number) => {
        return parseFloat(number + "").toFixed(2);
    }

    return (
        <Marker icon={customMarkerIcon} position={props.position}>
                <Tooltip interactive={true} eventHandlers={{
            click:(e) => {
                const text = `${roundDown(props.position[0])},${roundDown(props.position[1])}`;
                console.log(`Copied to clipboard: ${text}`)
                navigator.clipboard.writeText(text);
            }
          }} className='mapLabel' offset={[40, 5]} direction="bottom" opacity={1} permanent>
                    <span>[{roundDown(props.position[0])},{roundDown(props.position[1])}]</span>
                </Tooltip>
           </Marker> 
      );
  }
  
  export default MapLabel;