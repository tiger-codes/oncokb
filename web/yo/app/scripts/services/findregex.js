'use strict';

/**
 * @ngdoc service
 * @name oncokb.GenerateReportDataService
 * @description
 * # GenerateReportDataService
 * Factory in the oncokb.
 */
angular.module('oncokbApp')
  .factory('FindRegex', function () {
    function find(str, type) {
        if(typeof str === 'string' && str !== '') {
            var regex = [/PMID:\s*([0-9]+,*\s*)+/ig, /NCT[0-9]+/ig],
                links = ['http://www.ncbi.nlm.nih.gov/pubmed/',
                         'http://clinicaltrials.gov/show/'];
            for (var j = 0, regexL = regex.length; j < regexL; j++) {
                var result = str.match(regex[j]);

                if(result) {
                    /*jshint -W083 */
                    var uniqueResult = result.filter(function(elem, pos) {
                        return result.indexOf(elem) === pos;
                    });
                    /*jshint +W083 */
                    for(var i = 0, resultL = uniqueResult.length; i < resultL; i++) {
                        var _datum = uniqueResult[i];

                        switch(j) {
                            case 0:
                                var _number = _datum.split(':')[1].trim();
                                _number = _number.replace(/\s+/g, '');
                                str = str.replace(new RegExp(_datum, 'g'), createTag(type, links[j] + _number, _datum));
                                break;
                            default:
                                str = str.replace(_datum, createTag(type, links[j] + _datum, _datum));
                                break;
                        }

                    }
                }
            }
        }
        return str;
    }

    function createTag(type, link, content){
        var str = '';
        switch (type) {
            case 'link':
                str = '<a class="withUnderScore" target="_blank" href="'+ link +'">' + content + '</a>';
                break;
            case 'iframe':
                str = '<div contenteditable="false" tooltip-html-unsafe="<iframe src=\'' + link + '\'></iframe>">' + content + '</div>';
                break;
            default:
                break;
        }
        return str;
    }
    // Public API here
    return {
      get: function(str){ return find(str, 'link')},
      iframe: function(str){ return find(str, 'iframe')}
    };
  });