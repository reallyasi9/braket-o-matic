import JSDOM from 'jsdom';
import { select } from 'd3';
import { writeFileSync } from 'fs';

const dom = new JSDOM.JSDOM('<!DOCTYPE html><body></body>');

const canvasHeight = 1080;
const canvasWidth = 1920;

let body = select(dom.window.document.querySelector('body'));
let svg = body.append('svg')
    .attr('width', canvasWidth)
    .attr('height', canvasHeight)
    .attr('xmlns', 'http://www.w3.org/2000/svg');

const vMargin = 4;
const leafGames = 16;
const gameHeight = (canvasHeight + vMargin) / leafGames - vMargin;

const hMargin = 24;
const columns = 9;
const gameWidth = (canvasWidth + hMargin) / columns - hMargin;

const iconWidth = (gameHeight - 2) / 2;
const iconHeight = iconWidth;

function addGame(svg, col, game, x, y) {
    svg.append('rect')
        .attr('x', x)
        .attr('y', y)
        .attr('width', gameWidth)
        .attr('height', gameHeight)
        .attr('stroke', '#000000')
        .attr('fill', '#ffffff')
        .attr('id', `round-${col}-game-${game}-game`);
    svg.append('rect')
        .attr('x', x + 1)
        .attr('y', y + 1)
        .attr('width', iconWidth)
        .attr('height', iconHeight)
        .attr('fill', 'orange')
        .attr('id', `round-${col}-game-${game}-team-0-icon`);
    svg.append('text')
        .attr('x', x + iconWidth + 1)
        .attr('y', y + 1)
        .attr('width', gameWidth - iconWidth - 1)
        .attr('height', iconHeight)
        .attr('fill', 'purple')
        .attr('id', `round-${col}-game-${game}-team-0-name`);
    svg.append('rect')
        .attr('x', x + 1)
        .attr('y', y + gameHeight / 2 + 1)
        .attr('width', iconWidth)
        .attr('height', iconHeight)
        .attr('fill', 'purple')
        .attr('id', `round-${col}-game-${game}-team-1-icon`);
    svg.append('text')
        .attr('x', x + iconWidth + 1)
        .attr('y', y + iconHeight + 3)
        .attr('width', gameWidth - iconWidth - 1)
        .attr('height', iconHeight)
        .attr('fill', 'purple')
        .attr('id', `round-${col}-game-${game}-team-1-name`);
}

// left/right
const lr = [true, false];
for (let left of lr) {
    const xPad = left ? 0 : 8*(gameWidth + hMargin);
    const xDir = left ? 1 : -1;
    for (let col = 0; col < 4; col++) {
        const rows = 2**(4 - col);
        const ySkip = 2**col;
        const yPad = (ySkip - 1) / 2;
        for (let i = 0; i < rows; i++) {
            // 0 + 0, .5 + 1, 1.5 + 3, 3.5 + 7
            const y = (yPad + i * ySkip) * (gameHeight + vMargin);
            const x = xPad + xDir * col * (gameWidth + hMargin);
            const game = left ? i : i+rows;
            // process.stdout.write(`col ${col} i ${i} ySkip ${ySkip} yPad ${yPad} y ${y} x ${x}\n`)
            addGame(svg, col, game, x, y);
        }
    }
}

// semifinals left on top, semifinals right on bottom, finals in middle
let yPad = 5.5;
let xPad = 4*(gameWidth + hMargin);
let y = yPad * (gameHeight + vMargin);
let x = xPad - hMargin/2;
addGame(svg, 4, 0, x, y);

y += 4*(gameHeight + vMargin);
x += hMargin;
addGame(svg, 4, 1, x, y);

y -= 2*(gameHeight + vMargin);
x -= hMargin/2;
addGame(svg, 5, 0, x, y);

writeFileSync('out.svg', body.html());
