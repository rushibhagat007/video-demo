import { ViewChild } from '@angular/core';
import { Component, OnInit  } from '@angular/core';

@Component({
  selector: 'app-video',
  templateUrl: './video.component.html',
  styleUrls: ['./video.component.css']
})

export class VideoComponent implements OnInit {

  @ViewChild('videoPlayer') videoplayer: any;

  constructor() { 
  }

  ngOnInit() {
  }

  pauseVideo(event: any) {
    this.videoplayer.nativeElement.pause();
  }

  playVideo(event: any) {
    this.videoplayer.nativeElement.play();
    this.videoplayer.isEnded = false;
  }

 /* keyFunc(event: any){
  document.onkeydown = function(event) {
    switch (event.keyCode) {
       case 38:
            event.preventDefault();
            let vol = this.videoplayer.volume;
            if (vol!=1) {
              try {
                this.videoplayer.volume = vol + 0.02;
              }
              catch(err) {
                this.videoplayer.volume = 1;
              }
              
            }
            
          break;
       case 40:
            event.preventDefault();
             vol = this.videoplayer.volume;
            if (vol!=0) {
              try {
                this.videoplayer.volume = vol - 0.02;
              }
              catch(err) {
                this.videoplayer.volume = 0;
              }
              
            }
          break;
    }
  }
  } */
}
