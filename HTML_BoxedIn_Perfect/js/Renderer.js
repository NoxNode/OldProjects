var gRenderer = {};

function InitRenderer() {
    if(!gRenderer.initialized) {
        gRenderer.initialized = true;
        
        gRenderer.width = 1366;
        gRenderer.height = 768;
        gRenderer.backgroundColor = 0xFFFFFFFF;
        gRenderer.renderer = PIXI.autoDetectRenderer(gRenderer.width, gRenderer.height, { backgroundColor: gRenderer.backgroundColor, antialias: true });
        gRenderer.canvas = gRenderer.renderer.view;
    	gRenderer.canvas.style.width = window.innerWidth + "px";
    	gRenderer.canvas.style.height = window.innerHeight + "px";
    	gRenderer.styleWidth = window.innerWidth;
    	gRenderer.styleHeight = window.innerHeight;
        document.body.appendChild(gRenderer.canvas);
        window.onresize = function() {
        	gRenderer.canvas.style.width = window.innerWidth + "px";
        	gRenderer.canvas.style.height = window.innerHeight + "px";
        	gRenderer.styleWidth = window.innerWidth;
        	gRenderer.styleHeight = window.innerHeight;
        };
    }
}
