
import React, { useEffect, useState } from "react";
import { MapContainer, Polyline, TileLayer } from "react-leaflet";
import GridLayer from "./ui/grid/GridLayer";
import Person from "./ui/markers/Person";
import Taxi from "./ui/markers/Taxi";
import RoutineMachine from "./RoutineMachine";

import Paper from '@mui/material/Paper';
import { Button, Card, CardContent, Checkbox, FormControl, FormControlLabel, FormLabel, Grid, Radio, RadioGroup, Slider, TextField, Typography } from "@mui/material";
import { styled } from '@mui/material/styles';
import PlaceholderLayer from "./ui/grid/PlaceholderLayer";
import MinimumDistanceSlider from "./components/minimum_distance_slider/MinimumDistanceSlider";
import AdminOverridesPanel from "./components/admin/admin-overrides-panel";
import AdminStatistics from "./components/admin/admin-statistics";
import LayerRouteAllObjects from "./ui/routes/LayerRouteAllObjects";
import LayerRouteSelectedObject from "./ui/routes/LayerRouteSelectedObject";


const fillBlueOptions = { fillColor: 'blue' }

const Mapp = (props) => {
  const [showRouting, setShowRouting] = useState(false);
  const [showGrid, setShowGrid] = useState(false);
  const [showPlaceholder, setShowPlaceholder] = useState(false);
  
  // Routes - all
  const [showRoutesAll, setShowRoutesAll] = useState(false);
  const handleShowRoutesAll = () => {
    setShowRoutesAll(!showRoutesAll)
  }
  const [showRoutesAllForceRerender, setShowRoutesAllForceRerender] = useState(0);
  const handleForceAllRoutesRerender = () => {
    setShowRoutesAllForceRerender(showRoutesAllForceRerender + 1)
  }

  // Routes - selected object
  const [showSelectedObject, setShowSelectedObject] = useState(true);
  const [selectedObject, setSelectedObject] = useState({});
  const onSelectObjectChange = (objectId, objectType) => {
    setSelectedObject({
      id: objectId,
      type: objectType
    });
  }
  const handleShowSelectObject = () => {
    setShowSelectedObject(!showSelectedObject)
  }

  const [placeholderType, setPlaceholderType] = useState('nearest');


  const [center, setCenter] = useState([52.5200, 13.4050])
  const [viewportObjectsIds, setViewportObjectsIds] = useState([]);
  const [viewportObjects, setViewportObjects] = useState([]);

  


  /*  VIEWPORT OBJECT IDS*/
  const arraysContainSame = (a, b) => {
    a = Array.isArray(a) ? a : [];
    b = Array.isArray(b) ? b : [];
    return a.length === b.length && a.every(el => b.includes(el));
  }

  useEffect(() => {
    const intervalId = setInterval(() => {

      const request = {
        lat: center[0],
        lng: center[1],
        objects: ["PERSON", "PASSANGER"],
        size: "SIZE_3",
        layer: "ALL"
      }

      const params = getParam(request)

      fetch(`http://localhost:8080/viewport/objects-in-area?${params}`)
        .then(response => response.json())
        .then(data => {
          setViewportObjects(data)
        })
    }, 1000);

    return () => clearInterval(intervalId); //This is important
  }, [center])



  const [marker, setMarker] = useState([52.5200, 13.4050]);


  const getParam = (o, searchParam = new URLSearchParams) => {
    Object.entries(o).forEach(([k, v]) => {
      if (v !== null && typeof v === 'object')
        getParam(v, searchParam)
      else
        searchParam.append(k, v)
    })
    return searchParam
  }


  const handleOnMoveEnd = (coordinate) => {
    setCenter([coordinate.lat, coordinate.lng]);

    // updateViewer(coordinate.lng, coordinate.lat)
  }



  const elements = viewportObjects.map((element, index) => {

    const lat = element.coordinate.lat;
    const lng = element.coordinate.lng;
    const coordinate = [lat, lng];


    if (element.type === "TAXI") {
      return <Taxi key={index} id={element.id} position={coordinate} status={element.status} onSelect={onSelectObjectChange} type="TAXI"></Taxi>
    } else if (element.type === "PASSANGER") {
      return <Person key={index} id={element.id} position={coordinate} status={element.status} type="PASSANGER"></Person>
    }
  })



  const handleShowRouting = (event) => {
    setShowRouting(!showRouting)
  }
  const handleShowGridChange = (event) => {
    setShowGrid(!showGrid);
  };
  const handleShowPlaceholders = (event) => {
    setShowPlaceholder(!showPlaceholder)
  };
  const handelPlaceholderTypeChange = (event) => {
    setPlaceholderType(event.target.value)
  };
  const handelViewFilter = (event) => {
    console.log(event);
  };




  return (
    <>





      <Card className="admin-panel" variant="outlined">
        <CardContent>

          <Grid container>
            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Grid</FormLabel>
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showGrid} onChange={handleShowGridChange} />} label="Show grid" />
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showRouting} onChange={handleShowRouting} />} label="Show routing" />
            </Grid>


            {/* Routes */}
            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">All routes</FormLabel>
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showRoutesAll} onChange={handleShowRoutesAll} />} label="Show routes" />
            </Grid>
            <Grid item xs={6}>
              <Button fullWidth variant="outlined" onClick={handleForceAllRoutesRerender}>Reload</Button>
            </Grid>

            {/* SelectedObject */}
            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Selected object</FormLabel>
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showSelectedObject} onChange={handleShowSelectObject} />} label="Show" />
            </Grid>
            <Grid item xs={6}>
              <Button fullWidth variant="outlined" onClick={handleForceAllRoutesRerender}>Reload</Button>
            </Grid>


            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Placeholder</FormLabel>
            </Grid>
            <Grid item xs={12}>
              <FormControlLabel control={<Checkbox checked={showPlaceholder} onChange={handleShowPlaceholders} />} label="Show placeholders" />
            </Grid>
            <Grid item xs={12}>
              <RadioGroup className="flex-row" defaultValue="nearest" name="radio-buttons-group" onChange={handelPlaceholderTypeChange}>
                <Grid item xs={6}>
                  <FormControlLabel value="matrix" control={<Radio />} label="Matrix" />
                </Grid>
                <Grid item xs={6}>
                  <FormControlLabel value="nearest" control={<Radio />} label="Nearest" />
                </Grid>
              </RadioGroup>
            </Grid>

            <Grid item xs={12}>
              Selected id:
              {JSON.stringify(selectedObject)}
            </Grid>

            <Grid item xs={12}>
              <AdminOverridesPanel />
            </Grid>

          </Grid>
        </CardContent>
      </Card>

      <AdminStatistics />

      <MapContainer
        doubleClickZoom={false}
        preferCanvas={true}
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

        {showPlaceholder && (<PlaceholderLayer placeholderType={placeholderType}></PlaceholderLayer>)}
        {showGrid && (<GridLayer onMoveEnd={handleOnMoveEnd}></GridLayer>)}

        {/* Routes */}
        {showRoutesAll && (<LayerRouteAllObjects forceRerender={showRoutesAllForceRerender}/>)} 
        {/* Selected Object */}
        {showSelectedObject && (<LayerRouteSelectedObject selectedObject={selectedObject}/>)} 
        

        {elements}

        {showRouting && <RoutineMachine />}
      </MapContainer>


    </>
  );
};

export default Mapp;
