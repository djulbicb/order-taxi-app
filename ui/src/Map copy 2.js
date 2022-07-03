import React, { useEffect, useState } from "react";
import { MapContainer, Marker, Polyline, Popup, TileLayer, useMapEvents } from "react-leaflet";
import RoutineMachine from "./RoutineMachine";
import Taxi from "./ui/markers/Taxi";


const fillBlueOptions = { fillColor: 'blue' }
const redBlueOptions = { fillColor: 'red' }
const polyline = [
  [52.5200, 13.4050],
  [52.51, 13.5050]
]

const Map = (props) => {

  const [pos, setPos] = useState([]);
  const [allMarkers, setAllMarkers] = useState([]);

  const headers =  {
    Accept: 'application/json',
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*'
  }
  
  useEffect(()=> {
    fetch('http://localhost:8080/waypoints', headers)
          .then(response => response.json())
          .then(data => {
            setPos(data)
            console.log(data);
          })
  }, [])

  const [marker, setMarker] = useState([52.5200, 13.4050]);
  // UNCOMMENT THIS
  // const runFunc = (quote) => {
  //   console.log(quote)
  // }
  // useEffect(() => {
  //   setInterval(() => {
  //     runFunc("test")
  //     // setMarker([52.4200, 13.3050])

  //     fetch('http://localhost:8080/point', headers)
  //         .then(response => response.json())
  //         .then(data => {
  //           setMarker(data)
  //           console.log(data);
  //         })


  //   }, 500);
  // },[])

  useEffect(() => {
    fetch('http://localhost:8080/coordinates', headers)
    .then(response => response.json())
    .then(data => {
      setAllMarkers(data)
      // console.table(data);
    })
  },[])


  // [marker[0], marker[1]]
  // [52.6200, 13.5050]
  const markers = allMarkers.map(mark=> {
    const t = mark.split(",");
    return (
      <Taxi position={t} key={mark}></Taxi>
    )
  });


  

  return (
    <MapContainer
      doubleClickZoom={false}
      
      id="mapId"
      zoom={14}
      center={[52.5200, 13.4050]}
    >

      <TileLayer 
      
        // url="http://localhost:5000/tile/v1/car/tile({x},{y},{z}).mvt"
        // url="http://localhost:8080/tile/v1/car/tile({x},{y},{z})"
        url="http://localhost:9001/tile/{z}/{x}/{y}.png"
        // url="https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}"
        attribution="Tiles &copy; Esri &mdash; Sources: GEBCO, NOAA, CHS, OSU, UNH, CSUMB, National Geographic, DeLorme, NAVTEQ, and Esri"
      />
      <Polyline pathOptions={redBlueOptions} positions={polyline} />
      <Polyline pathOptions={fillBlueOptions} positions={pos} />

        <Marker position={marker} eventHandlers={{
        popupopen: (e) => {
          console.log(e)
        },
        popupclose: (e) => {
          console.log("close")
        }
      }}>
          <Popup>
            A pretty CSS3 popup. <br /> Easily customizable.
          </Popup>
       </Marker>

      {markers}

      <RoutineMachine />
    </MapContainer>
  );
};

export default Map;
