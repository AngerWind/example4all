!(function () {
  "use strict";
  var e = {
      485: function (e, n, t) {
        var i = t(645),
          o = t.n(i)()(function (e) {
            return e[1];
          });
        o.push([
          e.id,
          '* {\n  margin: 0;\n  padding: 0;\n  -webkit-box-sizing: border-box;\n          box-sizing: border-box;\n}\nbody {\n  font: bold 20px "Courier";\n}\n#main {\n  width: 360px;\n  height: 420px;\n  background-color: #b7d4a8;\n  margin: 100px auto;\n  border: 10px solid black;\n  border-radius: 40px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: vertical;\n  -webkit-box-direction: normal;\n      -ms-flex-flow: column;\n          flex-flow: column;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -ms-flex-pack: distribute;\n      justify-content: space-around;\n}\n#main #stage {\n  width: 304px;\n  height: 304px;\n  border: 2px solid black;\n  position: relative;\n}\n#main #stage #snake > div {\n  width: 10px;\n  height: 10px;\n  background-color: #000;\n  border: 1px solid #b7d4a8;\n  position: absolute;\n}\n#main #stage #food {\n  width: 10px;\n  height: 10px;\n  position: absolute;\n  left: 40px;\n  top: 100px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: horizontal;\n  -webkit-box-direction: normal;\n      -ms-flex-flow: row wrap;\n          flex-flow: row wrap;\n  -webkit-box-pack: justify;\n      -ms-flex-pack: justify;\n          justify-content: space-between;\n  -ms-flex-line-pack: justify;\n      align-content: space-between;\n}\n#main #stage #food > div {\n  width: 4px;\n  height: 4px;\n  background-color: black;\n  -webkit-transform: rotate(45deg);\n          transform: rotate(45deg);\n}\n#main #score-panel {\n  width: 300px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-pack: justify;\n      -ms-flex-pack: justify;\n          justify-content: space-between;\n}\n',
          "",
        ]),
          (n.Z = o);
      },
      645: function (e) {
        e.exports = function (e) {
          var n = [];
          return (
            (n.toString = function () {
              return this.map(function (n) {
                var t = e(n);
                return n[2] ? "@media ".concat(n[2], " {").concat(t, "}") : t;
              }).join("");
            }),
            (n.i = function (e, t, i) {
              "string" == typeof e && (e = [[null, e, ""]]);
              var o = {};
              if (i)
                for (var r = 0; r < this.length; r++) {
                  var a = this[r][0];
                  null != a && (o[a] = !0);
                }
              for (var s = 0; s < e.length; s++) {
                var c = [].concat(e[s]);
                (i && o[c[0]]) ||
                  (t &&
                    (c[2]
                      ? (c[2] = "".concat(t, " and ").concat(c[2]))
                      : (c[2] = t)),
                  n.push(c));
              }
            }),
            n
          );
        };
      },
      379: function (e, n, t) {
        var i,
          o = (function () {
            var e = {};
            return function (n) {
              if (void 0 === e[n]) {
                var t = document.querySelector(n);
                if (
                  window.HTMLIFrameElement &&
                  t instanceof window.HTMLIFrameElement
                )
                  try {
                    t = t.contentDocument.head;
                  } catch (e) {
                    t = null;
                  }
                e[n] = t;
              }
              return e[n];
            };
          })(),
          r = [];
        function a(e) {
          for (var n = -1, t = 0; t < r.length; t++)
            if (r[t].identifier === e) {
              n = t;
              break;
            }
          return n;
        }
        function s(e, n) {
          for (var t = {}, i = [], o = 0; o < e.length; o++) {
            var s = e[o],
              c = n.base ? s[0] + n.base : s[0],
              l = t[c] || 0,
              f = "".concat(c, " ").concat(l);
            t[c] = l + 1;
            var u = a(f),
              d = { css: s[1], media: s[2], sourceMap: s[3] };
            -1 !== u
              ? (r[u].references++, r[u].updater(d))
              : r.push({ identifier: f, updater: b(d, n), references: 1 }),
              i.push(f);
          }
          return i;
        }
        function c(e) {
          var n = document.createElement("style"),
            i = e.attributes || {};
          if (void 0 === i.nonce) {
            var r = t.nc;
            r && (i.nonce = r);
          }
          if (
            (Object.keys(i).forEach(function (e) {
              n.setAttribute(e, i[e]);
            }),
            "function" == typeof e.insert)
          )
            e.insert(n);
          else {
            var a = o(e.insert || "head");
            if (!a)
              throw new Error(
                "Couldn't find a.js style target. This probably means that the value for the 'insert' parameter is invalid."
              );
            a.appendChild(n);
          }
          return n;
        }
        var l,
          f =
            ((l = []),
            function (e, n) {
              return (l[e] = n), l.filter(Boolean).join("\n");
            });
        function u(e, n, t, i) {
          var o = t
            ? ""
            : i.media
            ? "@media ".concat(i.media, " {").concat(i.css, "}")
            : i.css;
          if (e.styleSheet) e.styleSheet.cssText = f(n, o);
          else {
            var r = document.createTextNode(o),
              a = e.childNodes;
            a[n] && e.removeChild(a[n]),
              a.length ? e.insertBefore(r, a[n]) : e.appendChild(r);
          }
        }
        function d(e, n, t) {
          var i = t.css,
            o = t.media,
            r = t.sourceMap;
          if (
            (o ? e.setAttribute("media", o) : e.removeAttribute("media"),
            r &&
              "undefined" != typeof btoa &&
              (i +=
                "\n/*# sourceMappingURL=data:application/json;base64,".concat(
                  btoa(unescape(encodeURIComponent(JSON.stringify(r)))),
                  " */"
                )),
            e.styleSheet)
          )
            e.styleSheet.cssText = i;
          else {
            for (; e.firstChild; ) e.removeChild(e.firstChild);
            e.appendChild(document.createTextNode(i));
          }
        }
        var h = null,
          p = 0;
        function b(e, n) {
          var t, i, o;
          if (n.singleton) {
            var r = p++;
            (t = h || (h = c(n))),
              (i = u.bind(null, t, r, !1)),
              (o = u.bind(null, t, r, !0));
          } else
            (t = c(n)),
              (i = d.bind(null, t, n)),
              (o = function () {
                !(function (e) {
                  if (null === e.parentNode) return !1;
                  e.parentNode.removeChild(e);
                })(t);
              });
          return (
            i(e),
            function (n) {
              if (n) {
                if (
                  n.css === e.css &&
                  n.media === e.media &&
                  n.sourceMap === e.sourceMap
                )
                  return;
                i((e = n));
              } else o();
            }
          );
        }
        e.exports = function (e, n) {
          (n = n || {}).singleton ||
            "boolean" == typeof n.singleton ||
            (n.singleton =
              (void 0 === i &&
                (i = Boolean(
                  window && document && document.all && !window.atob
                )),
              i));
          var t = s((e = e || []), n);
          return function (e) {
            if (
              ((e = e || []),
              "[object Array]" === Object.prototype.toString.call(e))
            ) {
              for (var i = 0; i < t.length; i++) {
                var o = a(t[i]);
                r[o].references--;
              }
              for (var c = s(e, n), l = 0; l < t.length; l++) {
                var f = a(t[l]);
                0 === r[f].references && (r[f].updater(), r.splice(f, 1));
              }
              t = c;
            }
          };
        };
      },
    },
    n = {};
  function t(i) {
    if (n[i]) return n[i].exports;
    var o = (n[i] = { id: i, exports: {} });
    return e[i](o, o.exports, t), o.exports;
  }
  (t.n = function (e) {
    var n =
      e && e.__esModule
        ? function () {
            return e.default;
          }
        : function () {
            return e;
          };
    return t.d(n, { a: n }), n;
  }),
    (t.d = function (e, n) {
      for (var i in n)
        t.o(n, i) &&
          !t.o(e, i) &&
          Object.defineProperty(e, i, { enumerable: !0, get: n[i] });
    }),
    (t.o = function (e, n) {
      return Object.prototype.hasOwnProperty.call(e, n);
    }),
    (function () {
      var e = t(379),
        n = t.n(e),
        i = t(485);
      function o(e, n) {
        for (var t = 0; t < n.length; t++) {
          var i = n[t];
          (i.enumerable = i.enumerable || !1),
            (i.configurable = !0),
            "value" in i && (i.writable = !0),
            Object.defineProperty(e, i.key, i);
        }
      }
      n()(i.Z, { insert: "head", singleton: !1 }), i.Z.locals;
      var r = (function () {
        function e() {
          !(function (e, n) {
            if (!(e instanceof n))
              throw new TypeError("Cannot call a.js class as a.js function");
          })(this, e),
            (this.element = document.getElementById("snake")),
            (this.head = document.querySelector("#snake > div")),
            (this.bodies = this.element.getElementsByTagName("div"));
        }
        var n, t;
        return (
          (n = e),
          (t = [
            {
              key: "addBody",
              value: function () {
                this.element.insertAdjacentHTML("beforeend", "<div></div>");
              },
            },
            {
              key: "moveBody",
              value: function () {
                for (var e = this.bodies.length - 1; e > 0; e--) {
                  var n = this.bodies[e - 1].offsetLeft,
                    t = this.bodies[e - 1].offsetTop;
                  (this.bodies[e].style.left = n + "px"),
                    (this.bodies[e].style.top = t + "px");
                }
              },
            },
            {
              key: "checkHeadBody",
              value: function () {
                for (var e = 1; e < this.bodies.length; e++) {
                  var n = this.bodies[e];
                  if (this.X === n.offsetLeft && this.Y === n.offsetTop)
                    throw new Error("撞到自己了！");
                }
              },
            },
            {
              key: "X",
              get: function () {
                return this.head.offsetLeft;
              },
              set: function (e) {
                if (this.X !== e) {
                  if (e < 0 || e > 290) throw new Error("蛇撞墙了！");
                  this.bodies[1] &&
                    this.bodies[1].offsetLeft === e &&
                    (e = e > this.X ? this.X - 10 : this.X + 10),
                    this.moveBody(),
                    (this.head.style.left = e + "px"),
                    this.checkHeadBody();
                }
              },
            },
            {
              key: "Y",
              get: function () {
                return this.head.offsetTop;
              },
              set: function (e) {
                if (this.Y !== e) {
                  if (e < 0 || e > 290) throw new Error("蛇撞墙了！");
                  this.bodies[1] &&
                    this.bodies[1].offsetTop === e &&
                    (e = e > this.Y ? this.Y - 10 : this.Y + 10),
                    this.moveBody(),
                    (this.head.style.top = e + "px"),
                    this.checkHeadBody();
                }
              },
            },
          ]) && o(n.prototype, t),
          e
        );
      })();
      function a(e, n) {
        for (var t = 0; t < n.length; t++) {
          var i = n[t];
          (i.enumerable = i.enumerable || !1),
            (i.configurable = !0),
            "value" in i && (i.writable = !0),
            Object.defineProperty(e, i.key, i);
        }
      }
      var s = (function () {
        function e() {
          !(function (e, n) {
            if (!(e instanceof n))
              throw new TypeError("Cannot call a.js class as a.js function");
          })(this, e),
            (this.element = document.getElementById("food"));
        }
        var n, t;
        return (
          (n = e),
          (t = [
            {
              key: "change",
              value: function () {
                Math.round(29 * Math.random());
                var e = 10 * Math.round(29 * Math.random());
                (this.element.style.left = e + "px"),
                  (this.element.style.top = e + "px");
              },
            },
            {
              key: "X",
              get: function () {
                return this.element.offsetLeft;
              },
            },
            {
              key: "Y",
              get: function () {
                return this.element.offsetTop;
              },
            },
          ]) && a(n.prototype, t),
          e
        );
      })();
      function c(e, n) {
        if (!(e instanceof n))
          throw new TypeError("Cannot call a.js class as a.js function");
      }
      function l(e, n) {
        for (var t = 0; t < n.length; t++) {
          var i = n[t];
          (i.enumerable = i.enumerable || !1),
            (i.configurable = !0),
            "value" in i && (i.writable = !0),
            Object.defineProperty(e, i.key, i);
        }
      }
      var f = (function () {
        function e() {
          var n =
              arguments.length > 0 && void 0 !== arguments[0]
                ? arguments[0]
                : 10,
            t =
              arguments.length > 1 && void 0 !== arguments[1]
                ? arguments[1]
                : 10;
          c(this, e),
            (this.score = 0),
            (this.level = 1),
            (this.scoreEle = document.getElementById("score")),
            (this.levelEle = document.getElementById("level")),
            (this.maxLevel = n),
            (this.upScore = t);
        }
        var n, t;
        return (
          (n = e),
          (t = [
            {
              key: "addScore",
              value: function () {
                (this.scoreEle.innerHTML = ++this.score + ""),
                  this.score % this.upScore == 0 && this.levelUp();
              },
            },
            {
              key: "levelUp",
              value: function () {
                this.level < this.maxLevel &&
                  (this.levelEle.innerHTML = ++this.level + "");
              },
            },
          ]) && l(n.prototype, t),
          e
        );
      })();
      function u(e, n) {
        for (var t = 0; t < n.length; t++) {
          var i = n[t];
          (i.enumerable = i.enumerable || !1),
            (i.configurable = !0),
            "value" in i && (i.writable = !0),
            Object.defineProperty(e, i.key, i);
        }
      }
      new ((function () {
        function e() {
          !(function (e, n) {
            if (!(e instanceof n))
              throw new TypeError("Cannot call a.js class as a.js function");
          })(this, e),
            (this.direction = ""),
            (this.isLive = !0),
            (this.snake = new r()),
            (this.food = new s()),
            (this.scorePanel = new f(10, 2)),
            this.init();
        }
        var n, t;
        return (
          (n = e),
          (t = [
            {
              key: "init",
              value: function () {
                document.addEventListener(
                  "keydown",
                  this.keydownHandler.bind(this)
                ),
                  this.run();
              },
            },
            {
              key: "keydownHandler",
              value: function (e) {
                this.direction = e.key;
              },
            },
            {
              key: "run",
              value: function () {
                var e = this.snake.X,
                  n = this.snake.Y;
                switch (this.direction) {
                  case "ArrowUp":
                  case "Up":
                    n -= 10;
                    break;
                  case "ArrowDown":
                  case "Down":
                    n += 10;
                    break;
                  case "ArrowLeft":
                  case "Left":
                    e -= 10;
                    break;
                  case "ArrowRight":
                  case "Right":
                    e += 10;
                }
                this.checkEat(e, n);
                try {
                  (this.snake.X = e), (this.snake.Y = n);
                } catch (e) {
                  alert(e.message + " GAME OVER!"), (this.isLive = !1);
                }
                this.isLive &&
                  setTimeout(
                    this.run.bind(this),
                    300 - 30 * (this.scorePanel.level - 1)
                  );
              },
            },
            {
              key: "checkEat",
              value: function (e, n) {
                e === this.food.X &&
                  n === this.food.Y &&
                  (this.food.change(),
                  this.scorePanel.addScore(),
                  this.snake.addBody());
              },
            },
          ]) && u(n.prototype, t),
          e
        );
      })())();
    })();
})();
