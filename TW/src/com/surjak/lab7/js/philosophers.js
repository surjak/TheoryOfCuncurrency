const fs = require('fs');

const STRATEGY = {
    NAIVE: 'naive',
    ASYMMETRIC: 'asymmetric',
    ARBITRATOR: 'arbitrator',
    STARVATION: 'starvation',
};

class Fork {
    constructor() {
        this.available = true;
    }

    async acquire() {
        await binaryExponentialBackoff(() => this.available, () => {
            this.available = false;
        });
    }

    release() {
        this.available = true;
    }
}

class Arbitrator {
    constructor(count) {
        this.count = count;
    }

    async acquire() {
        await binaryExponentialBackoff(() => this.count > 0, () => {
            this.count--;
        });
    }

    release() {
        this.count++;
    }
}

class Philosopher {
    constructor(number, leftFork, rightFork, arbitrator) {
        this.number = number;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.arbitrator = arbitrator;
        this.waitTimes = [];
    }

    async start(strategy, cycles) {
        const eatMethod = this.eatMethodByStrategy(strategy);

        for (let i = 0; i < cycles; i++) {
            this.log('thinking');

            const time = await execTimeNano(eatMethod);
            this.waitTimes.push(time);

            await delay(0);
        }
    }

    eatMethodByStrategy(strategy) {
        switch (strategy) {
            case STRATEGY.NAIVE:
                return this.eatNaive.bind(this);
            case STRATEGY.ASYMMETRIC:
                return this.eatAsymmetric.bind(this);
            case STRATEGY.ARBITRATOR:
                return this.eatArbitrator.bind(this);
            case STRATEGY.STARVATION:
                return this.eatStarvation.bind(this);
            default:
                throw new Error('Invalid strategy.');
        }
    }

    async eatNaive() {
        await this.leftFork.acquire();
        await this.rightFork.acquire();
        this.log('eating');
        this.rightFork.release();
        this.leftFork.release();
    }

    async eatAsymmetric() {
        if (this.number % 2 === 1) {
            await this.leftFork.acquire();
            await this.rightFork.acquire();
        } else {
            await this.rightFork.acquire();
            await this.leftFork.acquire();
        }
        this.log('eating');
        this.rightFork.release();
        this.leftFork.release();
    }

    async eatArbitrator() {
        await this.arbitrator.acquire();
        await this.leftFork.acquire();
        await this.rightFork.acquire();
        this.log('eating');
        this.rightFork.release();
        this.leftFork.release();
        this.arbitrator.release();
    }

    async eatStarvation() {
        await binaryExponentialBackoff(() => this.leftFork.available && this.rightFork.available, () => {
            this.log('eating');
        });
    }

    log(message) {
        console.log(`[Philosopher ${this.number}] ${message}`);
    }
}

async function binaryExponentialBackoff(predicate, callback) {
    for (let exp = 1; !predicate(); exp *= 2) {
        const delayMs = randomInteger(0, Math.min(exp, 2 ^ 10));
        await delay(delayMs);
    }
    callback();
}

function delay(milliseconds) {
    return new Promise((resolve, reject) => {
        setTimeout(resolve, milliseconds);
    });
}

function randomInteger(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
}

function times(n, fn) {
    return Array.from({length: n}, (_, i) => fn(i));
}

async function execTimeNano(fn) {
    const start = process.hrtime();
    await fn();
    const [seconds, nanoseconds] = process.hrtime(start);
    return seconds * 1e9 + nanoseconds;
}

async function run(n, strategy, cycles) {
    const forks = times(n, () => new Fork());
    const arbitrator = new Arbitrator(n - 1);
    const philosophers = times(n, i => new Philosopher(i + 1, forks[i], forks[(i + 1) % n], arbitrator));
    return Promise.all(
        philosophers.map(philosopher => philosopher.start(strategy, cycles))
    ).then(() => {
        const summary = {
            language: 'javascript',
            strategy: strategy,
            n: n,
            waitTimes: philosophers.map(philosopher => philosopher.waitTimes),
        };
        return summary;
    });
}

async function main() {
    const summaries = [];

    for (const strategy of [STRATEGY.ASYMMETRIC, STRATEGY.STARVATION, STRATEGY.ARBITRATOR]) {
        for (const n of [5, 10, 20]) {
            const summary = await run(n, strategy, 100);
            summaries.push(summary);
        }
    }

    return summaries;
}

main()
    .then(result => {
        const json = JSON.stringify(result, null, 2);
        fs.writeFileSync('js_result.json', json);
    })
    .catch(err => console.error(err));