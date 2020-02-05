require([ 'jquery', 'bsp-utils', '//cdn.jsdelivr.net/hls.js/latest/hls.min.js', '//cdn.plyr.io/2.0.12/plyr.js' ], function ($, bsp_utils, Hls, plyr) {

    bsp_utils.onDomInsert(document, '.plyr-container', {
        insert: function(plyrContainer) {

            let $plyrContainer = $(plyrContainer);

            // Target for all external events
            let $playerFrame = $plyrContainer.closest('.TimedContentPlayerNote-player-frame');

            // The plyr.io player element
            let $plyrPlayer = $plyrContainer.find('.plyr-player');

            // Load HLS streams specially
            let hlsUrl = $plyrPlayer.attr('data-hls-url');
            if (hlsUrl) {
                let hls = new Hls();
                hls.loadSource(hlsUrl);
                hls.attachMedia($plyrPlayer[0]);
            }

            // Initialize the plyr instance.
            let instances = plyr.setup($plyrPlayer[0], { autoplay: true });

            // Get the current instance
            let instance = getCurrentInstance(instances);

            // If we find the instance, attach our custom event handlers
            if (instance) {

                instance.on('timeupdate', function (event) {
                    let secondsElapsed = Math.round(event.detail.plyr.getCurrentTime());
                    $playerFrame.trigger('timeUpdate.timed', secondsElapsed);
                });

                instance.on('play', function(event) {

                    // Pause all other players when this one is played.
                    let otherInstances = getOtherInstances(plyr.get());

                    for (let i = 0; i < otherInstances.length; i++) {
                        otherInstances[i].pause();
                    }
                });

                function pause(event) {
                    instance.pause();
                }

                function seek(event, ts) {
                    instance.seek(Math.round(ts));
                    instance.play();
                }

                function destroy(event) {
                    $playerFrame.off('.timed');
                    instance.destroy();
                }

                $playerFrame.on('pause.timed', pause);
                $playerFrame.on('seek.timed', seek);
                $playerFrame.on('destroy.timed', destroy);
            }

            function getCurrentInstance(all) {
                let current;

                for (let i = 0; i < all.length; i++) {

                    if (all[i].getMedia() === $plyrPlayer[0]) {
                        current = all[i];
                        break;
                    }
                }

                return current;
            }

            function getOtherInstances(all) {
                let others = [];

                for (let i = 0; i < all.length; i++) {

                    if (all[i].getMedia() === $plyrPlayer[0]) {
                        continue;
                    }

                    others.push(all[i]);
                }

                return others;
            }
        }
    });
});
