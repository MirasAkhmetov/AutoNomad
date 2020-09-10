let myArr = [], myArr2 = [];

function loadData() {
    const Http = new XMLHttpRequest();
    const url='http://garage.test.auto-nomad.kz:8000/api/aparking/distinct-parking-zones/';
    Http.overrideMimeType("application/json");
    Http.open("GET", url, true);
    Http.setRequestHeader("Authorization", "72525168f90c516e6c4559a41e3987539f1d8b1f");
    Http.send();
    Http.onreadystatechange = (e) => {
        myArr = JSON.parse(Http.response);
        myArr = myArr.parking_zones;
        console.log('res myArr', myArr)
        parkingDraw()
    };

    const Http2 = new XMLHttpRequest();
    const url2 = 'http://garage.test.auto-nomad.kz:8000/api/aparking/parking_zones/?limit=170';
    Http2.overrideMimeType("application/json");
    Http2.open("GET", url2, true);
    Http2.setRequestHeader("Authorization", "72525168f90c516e6c4559a41e3987539f1d8b1f");
    Http2.send();
    Http2.onreadystatechange = (e) => {
        myArr2 = JSON.parse(Http2.response);
        myArr2 = myArr2.results;
        console.log('res myArr2', myArr2)
        parkingDraw()
    };

}

loadData()
parkingDraw()
// Http2.onload = loadData



console.log('res myArr2 outside', myArr2)

let map;
let geoLocation = null;
let geo;
let objectArray = []
let objectArrayLine = []
let clickedMarkerArray = []
let markerArray = []
let geoLocationMarker
let coordinatesStart = []
let coordinatesEnd = []
let coordinates = []
let polyline

function zoomIn() {
    map.zoomIn(1)
}

function zoomOut() {
    map.zoomOut(1)
}

function myGeoLocation() {
    console.log('myGeolocation', Android.giveMyGeoLocation())
    geoLocation = Android.giveMyGeoLocation()
    geoLocation = geoLocation.split(":")
    geo.setLatLng([geoLocation[0], geoLocation[1]])
    geo.addTo(map)
    map.setView([geoLocation[0], geoLocation[1]], 16)

    setInterval(() => {
        geoLocation = Android.giveMyGeoLocation()
        geoLocation = geoLocation.split(":")
        geo.setLatLng([geoLocation[0], geoLocation[1]])
    }, 5000)
}

function setMyGeoLocation(location) {
    console.log('setMyGeoLocation', location)
    geoLocation = location.split(":")
    geo.setLatLng([geoLocation[0], geoLocation[1]])
    geo.addTo(map)
    map.setView([geoLocation[0], geoLocation[1]], 16)

    setInterval(() => {
        geoLocation = Android.giveMyGeoLocation()
        geoLocation = geoLocation.split(":")
        geo.setLatLng([geoLocation[0], geoLocation[1]])
    }, 5000)
}

function openBySearch(id) {
    let res = myArr.find((parking) => parking.number === id)
    let index = myArr.findIndex((parking) => parking.number === id)
    map.setView([res.avg_lat - 0.003, res.avg_lon], 16)
    objectArray[index].setIcon(clickedMarkerArray[index].myIcon1).addTo(map)
    for(let j = 0; j < myArr.length; j++) {
        if(j === index){
            continue;
        }
        objectArray[j].setIcon(markerArray[j].myIcon1).addTo(map)
    }
    Android.showBottomSheet(id)
}

function clickedLine(id) {
    let indexes = []
    let res = myArr2.filter((parking) => parking.number === id)
    for(let j=0;j<res.length;j++){
        indexes.push(res[j].id - 2)
    }
    for(let j=0;j<myArr2.length;j++) {
        if(indexes.includes(j)){
            objectArrayLine[myArr2[j].id].setStyle({ color: '#1164B4', weight: 5 })
            continue;
        } else {
            objectArrayLine[myArr2[j].id].setStyle({ color: '#88B1D9', weight: 5 })
        }
    }
    Android.showBottomSheet(id)

}

map = L.map('mapid', {
    center: [43.272, 76.917],
    zoom: 12,
    zoomControl: false,
    fullscreenControl: false
});

// L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}',
// {attribution: 'Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community'}).addTo(map);


let tile = L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png',
    {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 19
    }).addTo(map);


geoLocationMarker = L.divIcon({
    iconSize: [0, 0],
    html: '<div class="pulse"><div class="circle"></div></div>'
})

geo = L.marker([43.272, 76.917], {icon: geoLocationMarker})

markerArray = [
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1001</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1002</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1003</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1004</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1005</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1007</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1008</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1009</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1011</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1012</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9001</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9002</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9003</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9004</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9005</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9006</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9008</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9009</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9010</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #D3DCE6; color: #313b46; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9011</div></div>'
        })
    },
]

clickedMarkerArray = [
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1001</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1002</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1003</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1004</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1005</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1007</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1008</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1009</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1011</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">1012</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9001</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9002</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9003</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9004</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9005</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9006</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9008</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9009</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9010</div></div>'
        })
    },
    {
        myIcon1: L.divIcon({
            iconSize: [0, 0],
            html: '<div style="position: relative;"><div  style="position: absolute; top: -14px; left: -15px; text-align: center; background-color: #2F82D2; color: white; width: 3rem; height: 1.1rem; border-radius: 7px; border: 0.125rem solid white; box-shadow: 1px 1px 4px 1px rgba(0,0,0,0.57);">9011</div></div>'
        })
    },
]

// setTimeout(() => {
    function parkingDraw() {
        if(myArr.length && myArr2.length){
            for(let i = 0; i < myArr.length; i++) {
                let markers = L.marker([myArr[i].avg_lat, myArr[i].avg_lon], {icon: markerArray[i].myIcon1})
                    .on('click', (e) => {
                        map.setView([e.latlng.lat - 0.003, e.latlng.lng], 16)
                    })
                    .addTo(map);

                objectArray.push(markers)

                markers.on('click', (e) => {
                    let id = myArr[i].number
                    objectArray[i].setIcon(clickedMarkerArray[i].myIcon1).addTo(map)
                    for(let j = 0; j < myArr.length; j++) {
                        if(j === i){
                            continue;
                        }
                        objectArray[j].setIcon(markerArray[j].myIcon1).addTo(map)
                    }
                    console.log(id)
                    Android.showBottomSheet(id)
                    clickedLine(id)

                })
            }

            for (let j = 0; j < myArr2.length; j++) {

                coordinatesStart[0] = myArr2[j].latitude_start
                coordinatesStart[1] = myArr2[j].longitude_start
                coordinatesEnd[0] = myArr2[j].latitude_end
                coordinatesEnd[1] = myArr2[j].longitude_end
                coordinates[0] = coordinatesStart
                coordinates[1] = coordinatesEnd
                polyline = L.polyline(coordinates, { color: '#88B1D9', weight: 5 })
                objectArrayLine.push(polyline)
                polyline.on('click', () => {
                    let id = myArr2[j].number
                    let res = myArr.find((parking) => parking.number === id)
                    let index = myArr.findIndex((parking) => parking.number === id)
                    map.setView([res.avg_lat - 0.003, res.avg_lon], 16)
                    objectArray[index].setIcon(clickedMarkerArray[index].myIcon1).addTo(map)
                    for(let k = 0; k < myArr.length; k++) {
                        if(k === index){
                            continue;
                        }
                        objectArray[k].setIcon(markerArray[k].myIcon1).addTo(map)
                    }
                    Android.showBottomSheet(id)
                    clickedLine(id)

                })
                .addTo(map)
            }
        }

    }


// }, 300)
